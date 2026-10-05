package com.yashika.SubscriptionbillingEngine.Repository;

import com.yashika.SubscriptionbillingEngine.Entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepo extends JpaRepository<Plan,Long>
{

}
