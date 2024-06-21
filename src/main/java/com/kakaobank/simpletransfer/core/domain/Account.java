package com.kakaobank.simpletransfer.core.domain;

import com.kakaobank.simpletransfer.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "balance", nullable = false, precision = 14, scale = 2)
    private BigDecimal balance;

    @Column(name = "transfer_limit_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal transferLimitAmount;

    @Column(name = "transfer_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal transferFee;

    public void checkWithdrawAvailable(
        BigDecimal totalWithdrawAmount,
        BigDecimal withdrawAmount
    ) {
        if (transferLimitExceeded(withdrawAmount, totalWithdrawAmount)) {
            throw new TransferLimitAmountExceedException();
        }
    }

    private boolean transferLimitExceeded(
        BigDecimal withdrawAmount,
        BigDecimal totalWithdrawAmount
    ) {
        var sum = totalWithdrawAmount.add(withdrawAmount);

        return transferLimitAmount.compareTo(sum) < 0;
    }

    public AccountTransaction withdraw(
        BigDecimal amount,
        String description,
        LocalDateTime baseDateTime
    ) {
        var totalWithdrawAmount = amount.add(transferFee);

        if (withdrawAmountExceededBalance(totalWithdrawAmount)) {
            throw new BalanceNotEnoughException();
        }

        this.balance = this.balance.min(totalWithdrawAmount);

        return AccountTransaction.builder()
            .accountId(this.id)
            .withdrawAmount(totalWithdrawAmount)
            .depositAmount(BigDecimal.ZERO)
            .balance(this.balance)
            .description(description)
            .transactionDate(baseDateTime.toLocalDate())
            .transactionTime(baseDateTime.toLocalTime())
            .build();
    }

    public AccountTransaction deposit(
        BigDecimal amount,
        String description
    ) {
        this.balance = this.balance.add(amount);

        var now = LocalDateTime.now();
        return AccountTransaction.builder()
            .accountId(this.id)
            .withdrawAmount(BigDecimal.ZERO)
            .depositAmount(amount)
            .balance(this.balance)
            .description(description)
            .transactionDate(now.toLocalDate())
            .transactionTime(now.toLocalTime())
            .build();
    }

    private boolean withdrawAmountExceededBalance(BigDecimal totalWithdrawAmount) {
        return this.balance.compareTo(totalWithdrawAmount) < 0;
    }

    public static class TransferLimitAmountExceedException extends RuntimeException {
    }

    public static class BalanceNotEnoughException extends RuntimeException {
    }
}
