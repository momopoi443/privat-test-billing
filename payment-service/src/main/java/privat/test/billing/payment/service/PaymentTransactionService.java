package privat.test.billing.payment.service;

import privat.test.billing.payment.dto.PaymentTransactionDto;

import java.util.List;

public interface PaymentTransactionService {

    PaymentTransactionDto createByRegularPayment(Long regularPaymentId);

    void reverse(Long id);

    List<PaymentTransactionDto> getAllByRegularPaymentId(Long regularPaymentId);

    PaymentTransactionDto getById(Long id);
}
