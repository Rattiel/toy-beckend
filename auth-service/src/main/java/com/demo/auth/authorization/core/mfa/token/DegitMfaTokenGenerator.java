package com.demo.auth.authorization.core.mfa.token;

import com.demo.auth.authorization.core.mfa.MfaDetails;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Random;

@Setter
@Getter
public class DegitMfaTokenGenerator implements MfaTokenGenerator {
    private Random random = new Random();

    private int tokenLifetime = 3000;

    @Override
    public MfaToken generate(MfaDetails details) {
        String code = this.createCode();
        LocalDateTime now = LocalDateTime.now();
        return MfaToken.builder()
                .verificationMethod(details.getVerificationMethod().getValue())
                .verificationAddress(details.getVerificationAddress())
                .code(code)
                .expiresAt(now.plusSeconds(getTokenLifetime()))
                .build();
    }

    private String createCode() {
        int number = random.nextInt(0, 9999);
        return String.format("%04d", number);
    }
}
