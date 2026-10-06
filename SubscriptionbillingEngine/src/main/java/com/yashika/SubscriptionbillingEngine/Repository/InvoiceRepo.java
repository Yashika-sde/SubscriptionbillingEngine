package com.yashika.SubscriptionbillingEngine.Repository;

import com.yashika.SubscriptionbillingEngine.Entity.Invoice;
import com.yashika.SubscriptionbillingEngine.Entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepo extends JpaRepository<Invoice,Long>
{
    boolean exitSubscriptionperiodStart(Subscription s, LocalDate periodStartDate);
    List<Invoice> findSubscriptionById(Long subscriptionId);
}
