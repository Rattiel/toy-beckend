package com.demo.auth.authorization.jackson2;

import com.demo.auth.authorization.Account;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class AuthServerJackson2Module extends SimpleModule {
    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(Account.class, AccountMixin.class);
    }
}
