package com.kakaobank.simpletransfer.core.service;

import com.kakaobank.simpletransfer.common.GUIDGenerator;
import com.kakaobank.simpletransfer.common.domain.BankCode;
import com.kakaobank.simpletransfer.core.domain.Account;
import com.kakaobank.simpletransfer.core.domain.AccountRepository;
import com.kakaobank.simpletransfer.core.domain.AccountTransactionRepository;
import com.kakaobank.simpletransfer.core.domain.Transfer;
import com.kakaobank.simpletransfer.core.domain.TransferRepository;
import com.kakaobank.simpletransfer.core.domain.service.DepositService;
import com.kakaobank.simpletransfer.core.infrastructure.BankMaintenanceTimeChecker;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransferService {

    private final BankMaintenanceTimeChecker bankMaintenanceTimeChecker;
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final AccountTransactionRepository accountTransactionRepository;

    private final DepositService depositService;
    //    private final ExternalBankDepositClient externalBankDepositClient;
    private final GUIDGenerator idGenerator = new GUIDGenerator();

    // TODO 출금계좌 Redis 분산락 트랜잭션 획득
    public Response transfer(final Request request) {
        checkBankMaintenanceTime(
            BankCode.KAKAO_BANK,
            request.receiverBankCode
        );

        // TODO 출금계좌 DB Lock 획득
        var baseDateTime = LocalDateTime.now();

        // TODO 출금 서비스로 분리하자
        final var senderAccount = accountRepository.findByAccountNumber(request.senderAccountNumber)
            .orElseThrow(AccountNotFoundException::new);

        final var transfer = transferRepository.save(
            Transfer.builder()
                .id(idGenerator.generate())
                .senderAccountId(senderAccount.getId())
                .receiverBankCode(request.receiverBankCode)
                .receiverAccountNumber(request.receiverAccountNumber)
                .transferAmount(request.amount)
                .status(Transfer.Status.CREATED)
                .build()
        );

        try {
            checkSenderAccount(
                senderAccount,
                request.amount,
                baseDateTime
            );

            // senderAccountTransaction 은 지금 저장하지 말고, 나중에 저장하자`
            final var senderAccountTransaction = accountTransactionRepository.save(
                // TODO id 랑 seq 지금 null 임, 넣는 로직 추가해야함
                senderAccount.withdraw(
                    request.amount,
                    request.senderDescription,
                    baseDateTime
                )
            );

            if (request.receiverBankCode == BankCode.KAKAO_BANK) {
                // TODO 당행 수취계좌 Redis 락 획득, DB 락 획득
                var receiverAccountTransaction = depositService.deposit(
                        new DepositService.Request(
                            request.receiverAccountNumber,
                            request.amount,
                            request.receiverDescription()
                        )
                    )
                    .orElseThrow(DepositFailedException::new);

                transfer.success(
                    senderAccountTransaction.getId(),
                    receiverAccountTransaction.getId()
                );
                // TODO 당행 수취계좌 Redis 락 획득, DB 락 획득
            } else {
                // TODO 타행 입금 요청
//                externalBankDepositClient.request(
//                        request.bankCode,
//                        request.accountId,
//                        request.amount
//                    )
//                    .orElseThrow(DepositFailedException::new);

                transfer.success(
                    senderAccountTransaction.getId(),
                    null
                );
            }

            // TODO 거래 출금 토픽 게시
        } catch (Exception e) {
            switch (e) {
                case Account.TransferLimitAmountExceedException ignored -> transfer.failed("이체한도초과");
                case Account.BalanceNotEnoughException ignored -> transfer.failed("잔액부족");
                case DepositFailedException ignored -> {
                    // TODO 출금 계좌로 다시 입금
                }
                default -> transfer.failed("이체실패");
            }

            // TODO 이체 실패 토픽

            // TODO 출금계좌 DB Lock 해제

            return new Response(
                false,
                null
            );
        }

        // TODO 이체 성공 토픽

        // TODO 출금계좌 DB Lock 해제

        return null;
    }
    // TODO 출금계좌 Redis 분산락 트랜잭션 커밋 & 해재

    private void checkBankMaintenanceTime(BankCode... bankCodes) {
        var isMaintenanceTime = Arrays.stream(bankCodes)
            .anyMatch(bankMaintenanceTimeChecker::check);

        if (isMaintenanceTime) {
            throw new BankMaintenanceTimeException();
        }
    }

    private void checkSenderAccount(
        Account senderAccount,
        BigDecimal transferAmount,
        LocalDateTime baseDateTime
    ) {
        var todayTotalTransferAmount = transferRepository.findAllBySenderAccountIdAndTransferredDateAndStatus(
                senderAccount.getId(),
                baseDateTime.toLocalDate(),
                Transfer.Status.TRANSFERRED
            )
            .stream()
            .map(Transfer::getTransferAmount)
            .reduce(BigDecimal::add)
            .orElseThrow();

        senderAccount.checkWithdrawAvailable(
            todayTotalTransferAmount,
            transferAmount
        );
    }

    public record Request(
        String senderAccountNumber,
        BankCode receiverBankCode,
        String receiverAccountNumber,
        BigDecimal amount,
        String senderDescription,
        String receiverDescription
    ) {
    }

    public record Response(
        boolean isSuccess,
        String transferId
    ) {
    }

    public static class BankMaintenanceTimeException extends RuntimeException {
    }

    public static class DepositFailedException extends RuntimeException {
    }

    // TODO 공통 영역으로 옮기기
    public static class AccountNotFoundException extends RuntimeException {
    }


}
