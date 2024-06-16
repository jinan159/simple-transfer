package com.kakaobank.simpletransfer.core.domain;

import com.kakaobank.simpletransfer.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "transfers")
@Getter
@Setter
public class Transfer extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "sender_account_number", nullable = false)
    private String senderAccountNumber;

    @Column(name = "receiver_account_number", nullable = false)
    private String receiverAccountNumber;

    @Column(name = "account_transaction_id", nullable = false)
    private String accountTransactionId;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "transfer_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal transferAmount;

    @Column(name = "requested_at", nullable = false)
    private ZonedDateTime requestedAt;

    @Column(name = "canceled_at")
    private ZonedDateTime canceledAt;

    public enum Status {
        CREATED,
        FAILED,
        COMPLETED
    }
}
