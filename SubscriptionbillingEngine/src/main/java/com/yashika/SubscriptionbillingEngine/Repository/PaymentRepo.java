package com.yashika.SubscriptionbillingEngine.Repository;

import com.yashika.SubscriptionbillingEngine.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.hibernate.boot.model.NamedEntityGraphDefinition.Source.JPA;

public interface PaymentRepo extends JpaRepository<Payment,Long> {
}
