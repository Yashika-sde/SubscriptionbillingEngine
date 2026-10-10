package com.yashika.SubscriptionbillingEngine.Service;

import com.yashika.SubscriptionbillingEngine.Entity.Enum.InvoiceStatus;
import com.yashika.SubscriptionbillingEngine.Entity.Enum.PaymentStatus;
import com.yashika.SubscriptionbillingEngine.Entity.Enum.Status;
import com.yashika.SubscriptionbillingEngine.Entity.Invoice;
import com.yashika.SubscriptionbillingEngine.Entity.Payment;
import com.yashika.SubscriptionbillingEngine.Entity.Subscription;
import com.yashika.SubscriptionbillingEngine.Repository.InvoiceRepo;
import com.yashika.SubscriptionbillingEngine.Repository.PaymentRepo;
import com.yashika.SubscriptionbillingEngine.Repository.SubscriptionRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PaymentService
{
    private static final int MAX_RETRIES = 3;

    private final PaymentRepo paymentRepo;
    private final InvoiceRepo invoiceRepo;
    private final SubscriptionRepo subscriptionRepo;

    public  PaymentService(PaymentRepo paymentRepo,InvoiceRepo invoiceRepo,SubscriptionRepo subscriptionRepo)
    {
        this.invoiceRepo = invoiceRepo;
        this.paymentRepo = paymentRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

    public void charge(Invoice inv)
    {
        boolean success = new Random().nextInt(100) < 70;

        Payment pay = new Payment();
        pay.setInvoice(inv);
        pay.setAmount(inv.getTotal());
        pay.setStatus(success? PaymentStatus.SUCCESS:PaymentStatus.FAILED);
        pay.setAttemptedOn(LocalDateTime.now());
        paymentRepo.save(pay);

        if(success)
        {
            inv.setStatus(InvoiceStatus.PAID);
        }
        else
        {
            inv.setRetryCount(inv.getRetryCount()+1);
            inv.setStatus(InvoiceStatus.FAILED);

            if(inv.getRetryCount() >= MAX_RETRIES) {
                Subscription sub = inv.getSubscription();
                sub.setStatus(Status.PAST_DUE);
                subscriptionRepo.save(sub);
            }
        }
        invoiceRepo.save(inv);
    }
}
