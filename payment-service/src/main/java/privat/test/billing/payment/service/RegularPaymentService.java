package privat.test.billing.payment.service;

import privat.test.billing.payment.dto.CreateRegularPaymentDto;
import privat.test.billing.payment.dto.RegularPaymentDto;

import java.util.List;

public interface RegularPaymentService {

    RegularPaymentDto create(CreateRegularPaymentDto dto);

    RegularPaymentDto update(RegularPaymentDto dto);

    void deactivate(Long id);

    RegularPaymentDto getById(Long id);

    List<RegularPaymentDto> getAllByPayerTaxNumber(String payerTaxNumber);

    List<RegularPaymentDto> getAllByRecipientEdrpou(String recipientEdrpou);

    List<RegularPaymentDto> getAll();

    Boolean isPending(Long id);

}
