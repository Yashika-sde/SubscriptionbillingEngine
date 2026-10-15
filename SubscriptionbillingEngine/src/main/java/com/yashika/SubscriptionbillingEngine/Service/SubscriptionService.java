package com.yashika.SubscriptionbillingEngine.Service;

import com.yashika.SubscriptionbillingEngine.Entity.Customer;
import com.yashika.SubscriptionbillingEngine.Entity.Enum.BillingCycle;
import com.yashika.SubscriptionbillingEngine.Entity.Enum.Status;
import com.yashika.SubscriptionbillingEngine.Entity.Invoice;
import com.yashika.SubscriptionbillingEngine.Entity.Plan;
import com.yashika.SubscriptionbillingEngine.Entity.Subscription;
import com.yashika.SubscriptionbillingEngine.Exception.BadRequestException;
import com.yashika.SubscriptionbillingEngine.Exception.ResourceNotFoundException;
import com.yashika.SubscriptionbillingEngine.Repository.CustomerRepo;
import com.yashika.SubscriptionbillingEngine.Repository.PlanRepo;
import com.yashika.SubscriptionbillingEngine.Repository.SubscriptionRepo;
import com.yashika.SubscriptionbillingEngine.dto.Subscriptiondto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class SubscriptionService
{
    private final PaymentService paymentService;
    private final CustomerRepo customerRepo;
    private final PlanRepo planRepo;
    private final SubscriptionRepo subscriptionRepo;
    private final InvoiceService invoiceService;

    public SubscriptionService(CustomerRepo customerRepo,PlanRepo planRepo,SubscriptionRepo subscriptionRepo,InvoiceService invoiceService,PaymentService paymentService)
    {
        this.paymentService = paymentService;
        this.customerRepo = customerRepo;
        this.planRepo = planRepo;
        this.subscriptionRepo = subscriptionRepo;
        this.invoiceService = invoiceService;
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

    public Subscription upgrade(Long id, Long newPlanId) {
        Subscription sub = subscriptionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));
        Plan newPlan = planRepo.findById(newPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));
        Plan oldPlan = sub.getPlan();

        if (sub.getStatus() != Status.ACTIVE)
            throw new BadRequestException("Only active subscriptions can be upgraded");
        if (newPlan.getPrice().compareTo(oldPlan.getPrice()) <= 0)
            throw new BadRequestException("New plan must cost more than the current plan");

        LocalDate today = LocalDate.now();
        LocalDate periodEnd = sub.getNextBillingDate();
        LocalDate periodStart = previousDate(periodEnd, oldPlan.getBillingCycle());

        long totalDays = ChronoUnit.DAYS.between(periodStart, periodEnd);
        long daysLeft = ChronoUnit.DAYS.between(today, periodEnd);

        BigDecimal credit = oldPlan.getPrice().multiply(BigDecimal.valueOf(daysLeft))
                .divide(BigDecimal.valueOf(totalDays), 2, RoundingMode.HALF_UP);
        BigDecimal charge = newPlan.getPrice().multiply(BigDecimal.valueOf(daysLeft))
                .divide(BigDecimal.valueOf(totalDays), 2, RoundingMode.HALF_UP);

        sub.setPlan(newPlan);
        subscriptionRepo.save(sub);

        Invoice inv = invoiceService.generateProrated(sub, charge.subtract(credit), today, periodEnd);
        paymentService.charge(inv);
        return sub;
    }

    private LocalDate previousDate(LocalDate from, BillingCycle cycle) {
        return switch (cycle) {
            case MONTHLY -> from.minusMonths(1);
            case QUARTELY -> from.minusMonths(3);
            case YEARLY -> from.minusYears(1);
        };
    }

}
