package privat.test.billing.payment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import privat.test.billing.payment.dto.CreateRegularPaymentDto;
import privat.test.billing.payment.dto.RegularPaymentDto;
import privat.test.billing.payment.service.RegularPaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/regular-payment")
@RequiredArgsConstructor
public class RegularPaymentController {

    private final RegularPaymentService regularPaymentService;

    @PostMapping
    public ResponseEntity<RegularPaymentDto> createRegularPayment(
            @Valid @RequestBody CreateRegularPaymentDto dto
    ) {
        var regularPaymentDto = regularPaymentService.create(dto);

        return ResponseEntity.ok(regularPaymentDto);
    }

    @PutMapping
    public ResponseEntity<RegularPaymentDto> createRegularPayment(
            @Valid @RequestBody RegularPaymentDto dto
    ) {
        var regularPaymentDto = regularPaymentService.update(dto);

        return ResponseEntity.ok(regularPaymentDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateRegularPayment(
            @Positive(message = "Regular payment id must be positive")
            @PathVariable Long id
    ) {
        regularPaymentService.deactivate(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegularPaymentDto> getRegularPaymentById(
            @Positive(message = "Regular payment id must be positive")
            @PathVariable Long id
    ) {
        var regularPaymentDto = regularPaymentService.getById(id);

        return ResponseEntity.ok(regularPaymentDto);
    }

    @GetMapping(params = "recipientEdrpou")
    public ResponseEntity<List<RegularPaymentDto>> getRegularPaymentsOfRecipient(
            @Pattern(regexp = "^\\d{8}$", message = "Recipient EDRPOU must be exactly 8 digits")
            @RequestParam String recipientEdrpou
    ) {
        var regularPaymentDtos = regularPaymentService.getAllByRecipientEdrpou(recipientEdrpou);

        return ResponseEntity.ok(regularPaymentDtos);
    }

    @GetMapping(params = "payerTaxNumber")
    public ResponseEntity<List<RegularPaymentDto>> getRegularPaymentsOfPayer(
            @Pattern(regexp = "^\\d{10}$", message = "Payer tax number must be exactly 10 digits")
            @RequestParam String payerTaxNumber
    ) {
        var regularPaymentDtos = regularPaymentService.getAllByPayerTaxNumber(payerTaxNumber);

        return ResponseEntity.ok(regularPaymentDtos);
    }

    @GetMapping
    public ResponseEntity<List<RegularPaymentDto>> getAllRegularPayments() {
        var regularPaymentDtos = regularPaymentService.getAll();

        return ResponseEntity.ok(regularPaymentDtos);
    }

    @GetMapping("/{id}/pending")
    public ResponseEntity<Boolean> isRegularPaymentPending(
            @Positive(message = "Regular payment id must be positive")
            @PathVariable Long id
    ) {
        var isPending = regularPaymentService.isPending(id);

        return ResponseEntity.ok(isPending);
    }
}
