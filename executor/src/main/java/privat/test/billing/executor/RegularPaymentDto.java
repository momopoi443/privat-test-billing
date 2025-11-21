package privat.test.billing.executor;

import java.math.BigDecimal;

public record RegularPaymentDto(
        Long id,
        String payerFullName,
        String payerTaxNumber,
        String payerCardNumber,
        String recipientAccount,
        String recipientBankCode,
        String recipientEdrpou,
        String recipientName,
        Long writeOffInterval,
        BigDecimal paymentAmount
) {}
