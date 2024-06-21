package com.kakaobank.simpletransfer.core.domain;

import com.kakaobank.simpletransfer.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account_transactions")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountTransaction extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(name = "sequence", nullable = false)
    private long sequence;

    @Column(name = "withdraw_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal withdrawAmount;

    @Column(name = "deposit_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal depositAmount;

    @Column(name = "balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal balance;

    @Column(name = "description")
    private String description;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "transaction_time")
    private LocalTime transactionTime;

    @Builder
    public AccountTransaction(
        String accountId,
        BigDecimal withdrawAmount,
        BigDecimal depositAmount,
        BigDecimal balance,
        String description,
        LocalDate transactionDate,
        LocalTime transactionTime
    ) {
        this.accountId = accountId;
        this.withdrawAmount = withdrawAmount;
        this.depositAmount = depositAmount;
        this.balance = balance;
        this.description = description;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
    }
}
