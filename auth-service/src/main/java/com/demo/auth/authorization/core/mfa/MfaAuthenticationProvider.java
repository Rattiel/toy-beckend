package com.demo.auth.authorization.core.mfa;

import com.demo.auth.authorization.core.mfa.token.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Slf4j
@Setter
public class MfaAuthenticationProvider implements AuthenticationProvider {
    private AuthenticationProvider delegate;

    private MfaTokenGenerator tokenGenerator = new DegitMfaTokenGenerator();

    private MfaCodePublisher publisher = new ConsoleMfaCodePublisher();

    private boolean ignoreCodeSendError = false;

    public MfaAuthenticationProvider(AuthenticationProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication result = this.delegate.authenticate(authentication);
        if (!(result.getPrincipal() instanceof MfaDetails mfaDetails)) {
            log.debug("MFA Authentication not supported principal [{}]", result.getPrincipal().getClass().getName());
            return result;
        }
        if (!mfaDetails.isMfaEnabled()) {
            log.debug("MFA Authentication is disabled");
            return result;
        }
        MfaToken token = tokenGenerator.generate(mfaDetails);
        log.debug("MFA code generated");
        try {
            if (publisher.support(token.getVerificationMethod())) {
                publisher.send(token);
            } else {
                log.error("MFA verification method not supported");
                return result;
            }
        } catch (Exception e) {
            log.debug("MFA code send error: {}", e.getMessage());
            if (ignoreCodeSendError) {
                return result;
            }
            throw e;
        }
        return new MfaNeedAuthenticationToken(result, token);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return this.delegate.supports(authentication);
    }
}
