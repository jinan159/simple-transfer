package com.kakaobank.simpletransfer.core.infrastructure;

import com.kakaobank.simpletransfer.common.domain.BankCode;
import org.springframework.stereotype.Component;

@Component
public class FakeBankMaintenanceTimeChecker implements BankMaintenanceTimeChecker {
    @Override
    public boolean check(BankCode bankCode) {
        return false;
    }
}
