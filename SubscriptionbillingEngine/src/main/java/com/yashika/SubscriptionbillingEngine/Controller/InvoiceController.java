package com.yashika.SubscriptionbillingEngine.Controller;

import com.yashika.SubscriptionbillingEngine.Entity.Invoice;
import com.yashika.SubscriptionbillingEngine.Entity.Subscription;
import com.yashika.SubscriptionbillingEngine.Repository.InvoiceRepo;
import com.yashika.SubscriptionbillingEngine.Repository.SubscriptionRepo;
import com.yashika.SubscriptionbillingEngine.Service.InvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/billing/Invoice")
@RestController
public class InvoiceController
{
    private final InvoiceService invoiceService;
    private final InvoiceRepo invoiceRepo;
    private final SubscriptionRepo subscriptionRepo;

    public InvoiceController(InvoiceService invoiceService,InvoiceRepo invoiceRepo,SubscriptionRepo subscriptionRepo)
    {
        this.invoiceRepo = invoiceRepo;
        this.invoiceService = invoiceService;
        this.subscriptionRepo = subscriptionRepo;
    }

    @PostMapping("/generate/{subscriptionId}")
    public Invoice generate(@PathVariable Long subscriptionId)
    {
        Subscription sub = subscriptionRepo.findById(subscriptionId)
                .orElseThrow(()->new RuntimeException("Subscription not found!"));
        return  invoiceService.generate(sub);
    }

    @GetMapping
    public List<Invoice> findBySubscriptionId(@RequestParam Long subscriptionId)
    {
        return invoiceRepo.findSubscriptionById(subscriptionId);
    }
}
