package com.demo.auth.web.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class RedirectUrlResolver implements HandlerMethodArgumentResolver {
    private final RequestCache requestCache;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(GetRedirectUrl.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        SavedRequest savedRequest = null;
        try {
            savedRequest = getSavedRequest(webRequest);
        } catch (Exception e) {
            log.error("Cannot resolve SavedRequest: {}", e.getMessage());
        }
        if (savedRequest != null) {
            return savedRequest.getRedirectUrl();
        }
        return getRedirectUrl(parameter);
    }

    private SavedRequest getSavedRequest(NativeWebRequest webRequest) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null || savedRequest.getRedirectUrl() == null) {
            return null;
        }
        return savedRequest;
    }

    private String getRedirectUrl(MethodParameter parameter) {
        GetRedirectUrl redirectUrl = parameter.getParameterAnnotation(GetRedirectUrl.class);
        if (redirectUrl == null) {
            return null;
        }
        return redirectUrl.value();
    }
}
