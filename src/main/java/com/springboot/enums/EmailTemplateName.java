package com.springboot.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate_account"),
    OTP_LOGIN("otp_login"),
    ACCOUNT_LOCKED("account_locked");

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
