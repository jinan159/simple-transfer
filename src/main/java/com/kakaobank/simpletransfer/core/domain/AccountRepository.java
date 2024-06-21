package com.kakaobank.simpletransfer.core.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByAccountNumber(String accountNumber);
}
