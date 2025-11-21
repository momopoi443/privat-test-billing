package privat.test.billing.payment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import privat.test.billing.payment.dto.PaymentTransactionDto;
import privat.test.billing.payment.entity.PaymentTransaction;
import privat.test.billing.payment.entity.TransactionStatus;
import privat.test.billing.payment.exception.NotFoundException;
import privat.test.billing.payment.repository.PaymentTransactionRepository;
import privat.test.billing.payment.repository.RegularPaymentRepository;
import privat.test.billing.payment.service.PaymentTransactionService;

import java.time.LocalDateTime;
import java.util.List;

import static privat.test.billing.payment.filter.RequestIdFilter.REQUEST_ID_KEY;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final RegularPaymentRepository regularPaymentRepository;

    @Override
    public PaymentTransactionDto createByRegularPayment(Long regularPaymentId) {
        log.info("Start creating payment transaction for regular payment with id: {}", regularPaymentId);

        var regularPayment = regularPaymentRepository.findByIdAndIsActiveTrue(regularPaymentId)
                .orElseThrow(() -> {
                    log.error("Could not find regular payment with id: {}", regularPaymentId);

                    return new NotFoundException("Could not find regular payment with id:" + regularPaymentId);
                });

        var paymentTransaction = new PaymentTransaction(
                null,
                LocalDateTime.now(),
                regularPayment,
                regularPayment.getPaymentAmount(),
                TransactionStatus.A
        );

        var createdPaymentTransaction = paymentTransactionRepository.save(paymentTransaction);
        log.info("Created payment transaction with id: {} for regular payment with id: {}",
                createdPaymentTransaction.getId(), regularPayment.getId());

        return mapPaymentTransactionToDto(paymentTransaction);
    }

    @Override
    public void reverse(Long id) {
        log.info("Start reversing payment transaction with id: {}", id);

        var paymentTransaction = paymentTransactionRepository.findById(id)
                .orElseThrow(() -> logAndCreateNotFoundException(id));

        paymentTransaction.setStatus(TransactionStatus.S);
        log.info("Reversing payment transaction with id: {}", id);
    }

    @Override
    public List<PaymentTransactionDto> getAllByRegularPaymentId(Long regularPaymentId) {
        log.info("Start fetching payment transactions for regular payment with id: {}", regularPaymentId);

        var paymentTransactions = paymentTransactionRepository.findAllByRegularPaymentId(regularPaymentId);

        return paymentTransactions.stream().map(this::mapPaymentTransactionToDto).toList();
    }

    @Override
    public PaymentTransactionDto getById(Long id) {
        log.info("Start fetching payment transaction with id: {}", id);

        var paymentTransaction = paymentTransactionRepository.findById(id)
                .orElseThrow(() -> logAndCreateNotFoundException(id));

        return mapPaymentTransactionToDto(paymentTransaction);
    }

    private PaymentTransactionDto mapPaymentTransactionToDto(PaymentTransaction paymentTransaction) {
        return new PaymentTransactionDto(
                paymentTransaction.getId(),
                paymentTransaction.getTransactionDateTime(),
                paymentTransaction.getPaymentAmount(),
                paymentTransaction.getStatus()
        );
    }

    private NotFoundException logAndCreateNotFoundException(Long id) {
        log.error("Could not find payment transaction with id: {}", id);

        throw new NotFoundException("Could not find payment transaction with id:" + id);
    }
}
