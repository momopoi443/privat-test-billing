package privat.test.billing.payment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import privat.test.billing.payment.dto.CreateRegularPaymentDto;
import privat.test.billing.payment.dto.RegularPaymentDto;
import privat.test.billing.payment.entity.PaymentTransaction;
import privat.test.billing.payment.entity.RegularPayment;
import privat.test.billing.payment.entity.TransactionStatus;
import privat.test.billing.payment.exception.NotFoundException;
import privat.test.billing.payment.repository.PaymentTransactionRepository;
import privat.test.billing.payment.repository.RegularPaymentRepository;
import privat.test.billing.payment.service.RegularPaymentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RegularPaymentServiceImpl implements RegularPaymentService {

    private final RegularPaymentRepository regularPaymentRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;

    @Override
    public RegularPaymentDto create(CreateRegularPaymentDto dto) {
        log.info("Start creating new regular payment with dto: {}", dto);

        var regularPayment = new RegularPayment(
                null,
                dto.payerFullName(),
                dto.payerTaxNumber(),
                dto.payerCardNumber(),
                dto.recipientAccount(),
                dto.recipientBankCode(),
                dto.recipientEdrpou(),
                dto.recipientName(),
                dto.writeOffInterval(),
                dto.paymentAmount(),
                true
        );

        var createdRegularPayment = regularPaymentRepository.save(regularPayment);
        log.info("Created regular payment with id: {}", regularPayment.getId());

        var paymentTransaction = new PaymentTransaction(
              null,
                LocalDateTime.now(),
                createdRegularPayment,
                dto.paymentAmount(),
                TransactionStatus.A
        );
        var createdPaymentTransaction = paymentTransactionRepository.save(paymentTransaction);
        log.info("Created initial payment transaction with id: {} for regular payment with id: {}",
                createdPaymentTransaction.getId(), regularPayment.getId());

        return mapRegularPaymentToDto(createdRegularPayment);
    }

    @Override
    public RegularPaymentDto update(RegularPaymentDto dto) {
        log.info("Start updating regular payment with dto: {}", dto);

        var regularPaymentToUpdate = regularPaymentRepository.findByIdAndIsActiveTrue(dto.id())
                .orElseThrow(() -> logAndCreateNotFoundException(dto.id()));

        regularPaymentToUpdate.setPayerFullName(dto.payerFullName());
        regularPaymentToUpdate.setPayerTaxNumber(dto.payerTaxNumber());
        regularPaymentToUpdate.setPayerCardNumber(dto.payerCardNumber());
        regularPaymentToUpdate.setRecipientAccount(dto.recipientAccount());
        regularPaymentToUpdate.setRecipientBankCode(dto.recipientBankCode());
        regularPaymentToUpdate.setRecipientEdrpou(dto.recipientEdrpou());
        regularPaymentToUpdate.setRecipientName(dto.recipientName());
        regularPaymentToUpdate.setWriteOffInterval(dto.writeOffInterval());
        regularPaymentToUpdate.setPaymentAmount(dto.paymentAmount());

        regularPaymentRepository.save(regularPaymentToUpdate);
        log.info("Updated regular payment with id: {}", dto.id());

        return dto;
    }

    @Override
    public void deactivate(Long id) {
        log.info("Start deactivation of regular payment with id: {}", id);

        var regularPaymentToDeactivate = regularPaymentRepository.findById(id)
                .orElseThrow(() -> logAndCreateNotFoundException(id));

        regularPaymentToDeactivate.setIsActive(false);

        regularPaymentRepository.save(regularPaymentToDeactivate);
        log.info("Deactivated regular payment with id: {}", id);
    }

    @Override
    public RegularPaymentDto getById(Long id) {
        log.info("Start fetching regular payment with id: {}", id);

        var regularPayment = regularPaymentRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> logAndCreateNotFoundException(id));

        return mapRegularPaymentToDto(regularPayment);
    }

    @Override
    public List<RegularPaymentDto> getAllByPayerTaxNumber(String payerTaxNumber) {
        log.info("Start fetching regular payments with payerTaxNumber: {}", payerTaxNumber);

        var regularPayments = regularPaymentRepository.findByPayerTaxNumberAndIsActiveTrue(payerTaxNumber);

        return regularPayments.stream().map(this::mapRegularPaymentToDto).toList();
    }

    @Override
    public List<RegularPaymentDto> getAllByRecipientEdrpou(String recipientEdrpou) {
        log.info("Start fetching regular payments with recipientEdrpou: {}", recipientEdrpou);

        var regularPayments = regularPaymentRepository.findByRecipientEdrpouAndIsActiveTrue(recipientEdrpou);

        return regularPayments.stream().map(this::mapRegularPaymentToDto).toList();
    }

    @Override
    public List<RegularPaymentDto> getAll() {
        log.info("Start fetching all regular payments");

        var regularPayments = regularPaymentRepository.findAll();

        return regularPayments.stream().map(this::mapRegularPaymentToDto).toList();
    }

    @Override
    public Boolean isPending(Long id) {
        log.info("Start checking if regular payment with id: {} is pending", id);

        var lastPaymentTransaction = paymentTransactionRepository
                .findMostRecentByRegularPaymentIdAndStatus(id).orElseThrow(() -> {
            log.error("Could not find most recent payment transaction for regular payment with id: {}", id);

            return new RuntimeException("Could not find most recent payment transaction for regular payment with id: " + id);
        });

        var timeOfWriteOff = lastPaymentTransaction.getTransactionDateTime()
                .plusSeconds(lastPaymentTransaction.getRegularPayment().getWriteOffInterval());

        return LocalDateTime.now().isAfter(timeOfWriteOff);
    }

    private RegularPaymentDto mapRegularPaymentToDto(RegularPayment regularPayment) {
        return new RegularPaymentDto(
                regularPayment.getId(),
                regularPayment.getPayerFullName(),
                regularPayment.getPayerTaxNumber(),
                regularPayment.getPayerCardNumber(),
                regularPayment.getRecipientAccount(),
                regularPayment.getRecipientBankCode(),
                regularPayment.getRecipientEdrpou(),
                regularPayment.getRecipientName(),
                regularPayment.getWriteOffInterval(),
                regularPayment.getPaymentAmount()
        );
    }

    private NotFoundException logAndCreateNotFoundException(Long id) {
        log.error("Could not find regular payment with id: {}", id);

        throw new NotFoundException("Could not find regular payment with id: " + id);
    }
}
