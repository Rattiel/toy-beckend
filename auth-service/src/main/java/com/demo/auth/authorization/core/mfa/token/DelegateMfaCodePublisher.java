package com.demo.auth.authorization.core.mfa.token;

import com.demo.auth.authorization.core.mfa.InvalidMfaRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class DelegateMfaCodePublisher implements MfaCodePublisher {
    private final List<MfaCodePublisher> delegates;

    public DelegateMfaCodePublisher(MfaCodePublisher... publishers) {
        Assert.notEmpty(publishers, "publishers cannot be empty");
        Assert.noNullElements(publishers, "publishers cannot be null");
        this.delegates = Collections.unmodifiableList(Arrays.asList(publishers));
    }

    @Override
    public void send(MfaToken mfaToken) {
        for (MfaCodePublisher publisher : delegates) {
            if (publisher.support(mfaToken.getVerificationMethod())) {
                publisher.send(mfaToken);
                return;
            }
        }
        log.error("No publisher found for verification method [{}]", mfaToken.getVerificationMethod());
        throw new InvalidMfaRequestException("No publisher found for verification method [" + mfaToken.getVerificationMethod() + "]");
    }
}
