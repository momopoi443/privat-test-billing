package privat.test.billing.payment.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import privat.test.billing.payment.dto.PaymentTransactionDto;
import privat.test.billing.payment.service.PaymentTransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-transaction")
@RequiredArgsConstructor
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> reversePaymentTransaction(
            @Positive(message = "Payment transaction id must be positive")
            @PathVariable Long id
    ) {
        paymentTransactionService.reverse(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<PaymentTransactionDto> createPaymentTransaction(
            @Positive(message = "Regular payment id must be positive")
            @RequestParam Long regularPaymentId
    ) {
        var paymentTransactionDto = paymentTransactionService.createByRegularPayment(regularPaymentId);

        return ResponseEntity.ok(paymentTransactionDto);
    }

    @GetMapping
    public ResponseEntity<List<PaymentTransactionDto>> getRegularPaymentTransactions(
            @Positive(message = "Regular payment id must be positive")
            @RequestParam Long regularPaymentId
    ) {
        var paymentTransactionDtos = paymentTransactionService.getAllByRegularPaymentId(regularPaymentId);

        return ResponseEntity.ok(paymentTransactionDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentTransactionDto> getPaymentTransactionById(
            @Positive(message = "Payment transaction id must be positive")
            @PathVariable Long id
    ) {
        var paymentTransactionDto = paymentTransactionService.getById(id);

        return ResponseEntity.ok(paymentTransactionDto);
    }
}
