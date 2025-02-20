package com.demo.auth.authorization.mfa.token;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleMfaCodePublisher implements MfaCodePublisher {
    @Override
    public void send(MfaToken token) {
        log.info("MFA code is [{}]", token.getCode());
    }
}
