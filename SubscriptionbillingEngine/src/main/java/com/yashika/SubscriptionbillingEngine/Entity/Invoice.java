package com.yashika.SubscriptionbillingEngine.Entity;

import com.yashika.SubscriptionbillingEngine.Entity.Enum.InvoiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter@Setter
@AllArgsConstructor@NoArgsConstructor
public class Invoice
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    private LocalDate periodStart;
    private LocalDate periodEnd;

    private BigDecimal amount;
    private BigDecimal tax;
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    private LocalDate issuedOn;
    private LocalDate dueOn;

    @Column(unique = false)
    private int retryCount = 0;
}
