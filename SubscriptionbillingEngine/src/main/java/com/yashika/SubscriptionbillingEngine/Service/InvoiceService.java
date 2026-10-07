package com.yashika.SubscriptionbillingEngine.Service;

import com.yashika.SubscriptionbillingEngine.Entity.Enum.InvoiceStatus;
import com.yashika.SubscriptionbillingEngine.Entity.Invoice;
import com.yashika.SubscriptionbillingEngine.Entity.Subscription;
import com.yashika.SubscriptionbillingEngine.Repository.InvoiceRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class InvoiceService
{
    private static final BigDecimal GST = new BigDecimal(0.18);
    private final InvoiceRepo invoiceRepo;

    public InvoiceService(InvoiceRepo invoiceRepo)
    {
        this.invoiceRepo = invoiceRepo;
    }

    public Invoice generate(Subscription sub)
    {
        LocalDate start = sub.getNextBillingDate();

        if(invoiceRepo.existsBySubscriptionAndPeriodStart(sub,start))
        {
            throw new RuntimeException("Invoice already exists!");
        }

        BigDecimal amount = sub.getPlan().getPrice();
        BigDecimal tax = amount.multiply(GST).setScale(2, RoundingMode.HALF_UP);

        Invoice inv = new Invoice();
        inv.setSubscription(sub);
        inv.setPeriodStart(start);
        inv.setPeriodEnd(start.plusMonths(1));
        inv.setAmount(amount);
        inv.setTax(tax);
        inv.setTotal(amount.add(tax));
        inv.setStatus(InvoiceStatus.PENDING);
        inv.setIssuedOn(LocalDate.now());
        inv.setDueOn(LocalDate.now().plusDays(7));
        return invoiceRepo.save(inv);
    }

}
