package com.kakaobank.simpletransfer.core.domain;

import com.kakaobank.simpletransfer.common.domain.BankCode;
import com.kakaobank.simpletransfer.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transfers")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transfer extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "sender_account_id", nullable = false)
    private String senderAccountId;

    @Column(name = "sender_account_transaction_id")
    private String senderAccountTransactionId;

    /*
        TODO 은행 시스템에서는 전문통신, 데이터양 최적화를 위해 enum 으로 저장한하겠지만, 원활한 구현을 위해 사용했다는것 작성하기
     */
    @Column(name = "receiver_bank_code", nullable = false)
    private BankCode receiverBankCode;

    @Column(name = "receiver_account_number", nullable = false)
    private String receiverAccountNumber;

    @Column(name = "receiver_account_transaction_id")
    private String receiverAccountTransactionId;

    @Column(name = "transfer_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal transferAmount;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "transferred_date")
    private LocalDate transferredDate;

    @Column(name = "transferred_time")
    private LocalTime transferredTime;

    @Column(name = "failed_date")
    private LocalDate failedDate;

    @Column(name = "failed_time")
    private LocalTime failedTime;

    @Column(name = "failed_reason")
    private String failedReason;

    @Builder
    public Transfer(
        String id,
        String senderAccountId,
        BankCode receiverBankCode,
        String receiverAccountNumber,
        BigDecimal transferAmount,
        Status status
    ) {
        this.id = id;
        this.senderAccountId = senderAccountId;
        this.receiverBankCode = receiverBankCode;
        this.receiverAccountNumber = receiverAccountNumber;
        this.transferAmount = transferAmount;
        this.status = status;
    }

    public void success(
        String senderTransactionId,
        String receiverTransactionId
    ) {
        this.status = Status.TRANSFERRED;
        this.senderAccountTransactionId = senderTransactionId;
        this.receiverAccountTransactionId = receiverTransactionId;

        var now = LocalDateTime.now();
        this.transferredDate = now.toLocalDate();
        this.transferredTime = now.toLocalTime();
    }

    public void failed(String failedReason) {
        this.status = Status.FAILED;

        var now = LocalDateTime.now();
        this.failedDate = now.toLocalDate();
        this.failedTime = now.toLocalTime();
        this.failedReason = failedReason;
    }

    public enum Status {
        CREATED,
        TRANSFERRED,
        FAILED
    }
}
