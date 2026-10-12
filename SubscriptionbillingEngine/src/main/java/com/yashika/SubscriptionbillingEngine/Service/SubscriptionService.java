package com.yashika.SubscriptionbillingEngine.Service;

import com.yashika.SubscriptionbillingEngine.Entity.Customer;
import com.yashika.SubscriptionbillingEngine.Entity.Enum.BillingCycle;
import com.yashika.SubscriptionbillingEngine.Entity.Enum.Status;
import com.yashika.SubscriptionbillingEngine.Entity.Plan;
import com.yashika.SubscriptionbillingEngine.Entity.Subscription;
import com.yashika.SubscriptionbillingEngine.Repository.CustomerRepo;
import com.yashika.SubscriptionbillingEngine.Repository.PlanRepo;
import com.yashika.SubscriptionbillingEngine.Repository.SubscriptionRepo;
import com.yashika.SubscriptionbillingEngine.dto.Subscriptiondto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SubscriptionService
{
    private final CustomerRepo customerRepo;
    private final PlanRepo planRepo;
    private final SubscriptionRepo subscriptionRepo;

    public SubscriptionService(CustomerRepo customerRepo,PlanRepo planRepo,SubscriptionRepo subscriptionRepo)
    {
        this.customerRepo = customerRepo;
        this.planRepo = planRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

    public Subscription create(Subscriptiondto req)
    {
        Customer customer = customerRepo.findById(req.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not Found!"));
        Plan plan = planRepo.findById(req.getPlanId())
                .orElseThrow(() -> new RuntimeException("Plan not Found!"));

        LocalDate startdate = LocalDate.now();

        Subscription sub = new Subscription();
        sub.setCustomer(customer);
        sub.setPlan(plan);
        sub.setStartDate(startdate);
        sub.setStatus(Status.ACTIVE);
        sub.setNextBillingDate(nextDate(startdate,plan.getBillingCycle()));
        return subscriptionRepo.save(sub);
    }

    private LocalDate nextDate(LocalDate from, BillingCycle billingCycle)
    {
        return switch (billingCycle){
            case MONTHLY -> from.plusMonths(1);
            case QUARTELY -> from.plusMonths(3);
            case YEARLY -> from.plusYears(1);
        };
    }

    public void advanceBillingDate(Subscription sub)
    {
        sub.setNextBillingDate(nextDate(sub.getNextBillingDate(),sub.getPlan().getBillingCycle()));
        subscriptionRepo.save(sub);
    }

    public Subscription cancel(Long id) {
        Subscription sub = subscriptionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        if (sub.getStatus() == Status.CANCELLED) {
            throw new RuntimeException("Subscription is already cancelled");
        }

        sub.setStatus(Status.CANCELLED);
        return subscriptionRepo.save(sub);
    }

}
