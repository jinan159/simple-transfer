package com.kakaobank.simpletransfer.common.domain;

public enum BankCode {
    KAKAO_BANK("090");

    BankCode(String code) {
        this.code = code;
    }

    public final String code;
}
