package privat.test.billing.payment.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateRegularPaymentDto(
        @NotBlank(message = "Payer full name is required")
        @Size(max = 255, message = "Payer full name must not exceed 255 characters")
        String payerFullName,

        @NotBlank(message = "Payer tax number is required")
        @Pattern(regexp = "^\\d{10}$", message = "Payer tax number must be exactly 10 digits")
        String payerTaxNumber,

        @NotBlank(message = "Payer card number is required")
        @Pattern(regexp = "^\\d{16}$", message = "Payer card number must be exactly 16 digits")
        String payerCardNumber,

        @NotBlank(message = "Recipient account is required")
        @Pattern(regexp = "^UA\\d{27}$", message = "Recipient account must be a valid Ukrainian IBAN")
        String recipientAccount,

        @NotBlank(message = "Recipient bank code is required")
        @Pattern(regexp = "^\\d{6}$", message = "Recipient bank code (MFO) must be exactly 6 digits")
        String recipientBankCode,

        @NotBlank(message = "Recipient EDRPOU is required")
        @Pattern(regexp = "^\\d{8}$", message = "Recipient EDRPOU must be exactly 8 digits")
        String recipientEdrpou,

        @NotBlank(message = "Recipient name is required")
        @Size(max = 255, message = "Recipient name must not exceed 255 characters")
        String recipientName,

        @NotNull(message = "Write-off interval is required")
        @Positive(message = "Write-off interval must be positive")
        Long writeOffInterval,

        @NotNull(message = "Regular payment amount is required")
        @Positive(message = "Regular payment amount must be positive")
        @Digits(integer = 13, fraction = 2, message = "Regular payment amount must have at most 13 integer digits and 2 decimal places")
        BigDecimal paymentAmount
) {}
