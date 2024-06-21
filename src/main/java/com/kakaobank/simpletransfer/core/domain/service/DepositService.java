package com.kakaobank.simpletransfer.core.domain.service;

import com.kakaobank.simpletransfer.core.domain.AccountRepository;
import com.kakaobank.simpletransfer.core.domain.AccountTransaction;
import com.kakaobank.simpletransfer.core.domain.AccountTransactionRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class DepositService {
    private final AccountRepository accountRepository;
    private final AccountTransactionRepository accountTransactionRepository;
    private final IdGenerator idGenerator;
    private final AccountTransactionSequence sequence;

    public Optional<AccountTransaction> deposit(Request request) {
        try {
            var account = accountRepository.findByAccountNumber(request.accountNumber)
                .orElseThrow(AccountNotFoundException::new);

            var depositTransaction = account.deposit(
                request.amount,
                request.description
            );

            depositTransaction.setId(idGenerator.generate());
            depositTransaction.setSequence(sequence.getNext(account.getId()));

            return Optional.of(
                accountTransactionRepository.save(
                    depositTransaction
                )
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public record Request(
        String accountNumber,
        BigDecimal amount,
        String description
    ) {
    }

    public static class AccountNotFoundException extends RuntimeException {
    }
}
