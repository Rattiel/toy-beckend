package com.demo.common.web;

import com.demo.common.exception.ExceptionMapper;
import com.demo.common.exception.ServiceException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ControllerAdvice(
        annotations = {
                Controller.class,
                RestController.class
        }
)
@Setter
@Controller
public class ServletExceptionHandler implements ErrorController {
    private ExceptionMapper exceptionMapper;

    private MessageSourceAccessor messageSourceAccessor;

    public ServletExceptionHandler(ExceptionMapper exceptionMapper) {
        this.exceptionMapper = exceptionMapper;
        this.messageSourceAccessor = new MessageSourceAccessor(
                new ErrorMassageSource(),
                LocaleContextHolder.getLocale()
        );
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSourceAccessor = new MessageSourceAccessor(
                messageSource,
                LocaleContextHolder.getLocale()
        );
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        String errorMessage = getErrorMessage(request);
        HttpStatus status = getErrorStatus(request);
        MessageResponse message = MessageResponse.builder()
                .status(status.value())
                .message(errorMessage)
                .messageDecoder(this::decodeMessage)
                .build();
        model.addAttribute("message", message);
        return "error";
    }

    private String getErrorMessage(HttpServletRequest request) {
        String errorMessage = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        return StringUtils.hasText(errorMessage) ? errorMessage : "Undefined Error";
    }

    private HttpStatus getErrorStatus(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            try {
                int code = Integer.parseInt(status.toString());
                return HttpStatus.valueOf(code);
            } catch (Exception ignored) {}
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<?> restExceptionHandler(Throwable exception) {
        ServiceException serviceException = exceptionMapper.apply(exception);
        MessageResponse body = MessageResponse.builder()
                .status(serviceException.getStatus())
                .message(serviceException.getMessage())
                .messageDecoder(this::decodeMessage)
                .build();
        return ResponseEntity.status(serviceException.getStatus())
                .body(body);
    }

    //@Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(
            exception = Throwable.class,
            produces = "text/html"
    )
    public String exceptionHandler(Throwable exception, Model model) {
        ServiceException serviceException = exceptionMapper.apply(exception);
        MessageResponse message = MessageResponse.builder()
                .status(serviceException.getStatus())
                .message(serviceException.getMessage())
                .messageDecoder(this::decodeMessage)
                .build();
        model.addAttribute("message", message);
        return "error";
    }

    private String decodeMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return this.messageSourceAccessor.getMessage(code, code, locale);
    }
}
