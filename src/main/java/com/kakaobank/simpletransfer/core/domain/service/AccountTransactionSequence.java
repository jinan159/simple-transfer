package com.kakaobank.simpletransfer.core.domain.service;

public interface AccountTransactionSequence {
    long getNext(String accountId);
}
