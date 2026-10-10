package com.yashika.SubscriptionbillingEngine.Scheduler;

import com.yashika.SubscriptionbillingEngine.Entity.Enum.Status;
import com.yashika.SubscriptionbillingEngine.Entity.Invoice;
import com.yashika.SubscriptionbillingEngine.Entity.Subscription;
import com.yashika.SubscriptionbillingEngine.Repository.SubscriptionRepo;
import com.yashika.SubscriptionbillingEngine.Service.InvoiceService;
import com.yashika.SubscriptionbillingEngine.Service.PaymentService;
import com.yashika.SubscriptionbillingEngine.Service.SubscriptionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BillingScheduler
{
    private final SubscriptionRepo subscriptionRepo;
    private final InvoiceService invoiceService;
    private final SubscriptionService subscriptionService;
    private final PaymentService paymentService;

    public BillingScheduler(SubscriptionService subscriptionService,SubscriptionRepo subscriptionRepo,InvoiceService invoiceService,PaymentService paymentService)
    {
        this.subscriptionRepo = subscriptionRepo;
        this.invoiceService = invoiceService;
        this.subscriptionService = subscriptionService;
        this.paymentService = paymentService;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void runBilling()
    {
        List<Subscription> due = subscriptionRepo
                .findByStatusAndNextBillingDateLessThanEqual(Status.ACTIVE, LocalDate.now());

        for(Subscription sub : due)
        {
            try
            {
                Invoice inv = invoiceService.generate(sub);
                paymentService.charge(inv);
                subscriptionService.advanceBillingDate(sub);
            }
            catch (Exception e)
            {
                System.out.println("Billing failed for Subscription!"+ sub.getId()+":"+e.getMessage());
            }
        }

        System.out.println("Scheduler ran on " + LocalDate.now() + ", due subscriptions: " + due.size());
    }
}
