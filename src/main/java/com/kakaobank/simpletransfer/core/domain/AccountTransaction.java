package com.kakaobank.simpletransfer.core.domain;

import com.kakaobank.simpletransfer.common.domain.BankCode;
import com.kakaobank.simpletransfer.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account_transactions")
@Getter
@Setter
public class AccountTransaction extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "type", nullable = false)
    private Type type;

    @Column(name = "sequence", nullable = false)
    private String sequence;

    @Enumerated(EnumType.STRING)
    @Column(name = "withdraw_bank_code", nullable = false)
    private BankCode withdrawBankCode;

    @Column(name = "withdraw_account_number", nullable = false)
    private String withdrawAccountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "deposit_bank_code", nullable = false)
    private BankCode depositBankCode;

    @Column(name = "deposit_account_number", nullable = false)
    private String depositAccountNumber;

    @Column(name = "withdraw_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal withdrawAmount;

    @Column(name = "deposit_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal depositAmount;

    @Column(name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(name = "description")
    private String description;

    @Column(name = "transaction_datetime", nullable = false)
    private ZonedDateTime transactionDatetime;

    public enum Type {
        WITHDRAW,
        DEPOSIT
    }
}
