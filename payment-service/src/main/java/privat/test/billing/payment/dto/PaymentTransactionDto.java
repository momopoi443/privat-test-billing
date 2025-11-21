package privat.test.billing.payment.dto;

import privat.test.billing.payment.entity.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentTransactionDto (
        Long id,
        LocalDateTime transactionDateTime,
        BigDecimal paymentAmount,
        TransactionStatus status
) {}
