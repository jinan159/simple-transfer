package com.kakaobank.simpletransfer.channel.domain;

import com.kakaobank.simpletransfer.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "simple_transfers")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimpleTransfer extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "guid", unique = true, nullable = false)
    private String guid;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(name = "sender_account_number", nullable = false)
    private String senderAccountNumber;

    @Column(name = "sender_transfer_id")
    private String senderTransferId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(name = "receiver_account_number")
    private String receiverAccountNumber;

    @Column(name = "receiver_transfer_id")
    private String receiverTransferId;

    @Column(name = "pending_account_number", nullable = false)
    private String pendingAccountNumber;

    @Column(name = "requested_at", nullable = false)
    private ZonedDateTime requestedAt;

    @Column(name = "received_at")
    private ZonedDateTime receivedAt;

    @Column(name = "canceled_at")
    private ZonedDateTime canceledAt;

    @Column(name = "expires_at", nullable = false)
    private ZonedDateTime expiresAt;

    public enum Status {
        CREATED,
        PENDING_WITHDRAW,
        PENDING_DEPOSIT,
        COMPLETED,
        CANCELED,
        FAILED,
        EXPIRED
    }
}
