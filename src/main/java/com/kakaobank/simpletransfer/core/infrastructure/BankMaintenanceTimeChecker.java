package com.kakaobank.simpletransfer.core.infrastructure;

import com.kakaobank.simpletransfer.common.domain.BankCode;

public interface BankMaintenanceTimeChecker {
    boolean check(BankCode bankCode);
}
