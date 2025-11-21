package privat.test.billing.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import privat.test.billing.payment.entity.RegularPayment;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegularPaymentRepository extends JpaRepository<RegularPayment, Long> {

    Optional<RegularPayment> findByIdAndIsActiveTrue(Long id);

    List<RegularPayment> findByPayerTaxNumberAndIsActiveTrue(String payerTaxNumber);

    List<RegularPayment> findByRecipientEdrpouAndIsActiveTrue(String recipientEdrpou);

}
