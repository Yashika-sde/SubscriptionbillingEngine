package com.yashika.SubscriptionbillingEngine.Repository;

import com.yashika.SubscriptionbillingEngine.Entity.Enum.Status;
import com.yashika.SubscriptionbillingEngine.Entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionRepo extends JpaRepository<Subscription,Long>
{
    List<Subscription> findByStatusAndNextBillingDateLessThanEqual(Status status, LocalDate date);
}
