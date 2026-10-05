package com.yashika.SubscriptionbillingEngine.Repository;

import com.yashika.SubscriptionbillingEngine.Entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepo extends JpaRepository<Subscription,Long>
{

}
