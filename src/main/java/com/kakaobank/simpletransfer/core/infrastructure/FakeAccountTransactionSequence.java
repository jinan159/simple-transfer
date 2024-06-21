package com.kakaobank.simpletransfer.core.infrastructure;

import com.kakaobank.simpletransfer.core.domain.service.AccountTransactionSequence;
import org.springframework.stereotype.Component;

@Component
public class FakeAccountTransactionSequence implements AccountTransactionSequence {
    @Override
    public long getNext(String accountId) {
        return 1;
    }
}
