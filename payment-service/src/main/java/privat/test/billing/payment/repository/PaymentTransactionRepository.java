package privat.test.billing.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import privat.test.billing.payment.entity.PaymentTransaction;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    @Query("""
        SELECT pt FROM PaymentTransaction pt
        WHERE pt.regularPayment.id = :regularPaymentId
        ORDER BY pt.transactionDateTime DESC
        LIMIT 1
    """)
    Optional<PaymentTransaction> findMostRecentByRegularPaymentIdAndStatus(
            @Param("regularPaymentId") Long regularPaymentId
    );

    List<PaymentTransaction> findAllByRegularPaymentId(Long regularPaymentId);
}
