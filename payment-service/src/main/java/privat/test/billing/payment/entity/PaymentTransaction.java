package privat.test.billing.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "payment_transactions", schema = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction {

    @Id
    @SequenceGenerator(
            name = "payment_transactions_seq_gen",
            sequenceName = "payment.payment_transactions_seq",
            schema = "payment",
            allocationSize = 50
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_transactions_seq_gen")
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "transaction_datetime", nullable = false)
    private LocalDateTime transactionDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regular_payment_id", nullable = false)
    private RegularPayment regularPayment;

    @Column(name = "payment_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PaymentTransaction student = (PaymentTransaction) o;
        return getId() != null && Objects.equals(getId(), student.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
