package com.yashika.SubscriptionbillingEngine.Entity;

import com.yashika.SubscriptionbillingEngine.Entity.Enum.BillingCycle;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter@Setter
@AllArgsConstructor@NoArgsConstructor
public class Plan
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private BillingCycle billingCycle;
}
