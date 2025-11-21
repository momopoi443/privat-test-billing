package privat.test.billing.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "regular_payments", schema = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegularPayment {

    @Id
    @SequenceGenerator(
            name = "regular_payments_seq_gen",
            sequenceName = "payment.regular_payments_seq",
            schema = "payment",
            allocationSize = 50
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regular_payments_seq_gen")
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "payer_full_name", nullable = false)
    private String payerFullName;

    @Column(name = "payer_tax_number", nullable = false, length = 10)
    private String payerTaxNumber;

    @Column(name = "payer_card_number", nullable = false, length = 16)
    private String payerCardNumber;

    @Column(name = "recipient_account", nullable = false, length = 29)
    private String recipientAccount;

    @Column(name = "recipient_bank_code", nullable = false, length = 6)
    private String recipientBankCode;

    @Column(name = "recipient_edrpou", nullable = false, length = 8)
    private String recipientEdrpou;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "write_off_interval", nullable = false)
    private Long writeOffInterval;

    @Column(name = "payment_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        RegularPayment student = (RegularPayment) o;
        return getId() != null && Objects.equals(getId(), student.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
