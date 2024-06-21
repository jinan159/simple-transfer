package com.kakaobank.simpletransfer.common;

import java.util.UUID;

public class GUIDGenerator {
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
