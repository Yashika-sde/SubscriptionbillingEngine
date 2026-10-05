package com.yashika.SubscriptionbillingEngine.Entity;

import com.yashika.SubscriptionbillingEngine.Entity.Enum.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter@Setter
@AllArgsConstructor@NoArgsConstructor
public class Subscription
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private LocalDate startDate;
    private LocalDate nextBillingDate;
}
