package com.kakaobank.simpletransfer.core.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, String> {
}
