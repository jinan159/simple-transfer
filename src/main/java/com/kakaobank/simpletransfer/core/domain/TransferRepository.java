package com.kakaobank.simpletransfer.core.domain;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, String> {
    List<Transfer> findAllBySenderAccountIdAndTransferredDateAndStatus(
        String senderAccountId,
        LocalDate transferredDate,
        Transfer.Status status
    );
}
