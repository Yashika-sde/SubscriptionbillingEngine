package com.yashika.SubscriptionbillingEngine.Controller;

import com.yashika.SubscriptionbillingEngine.Entity.Subscription;
import com.yashika.SubscriptionbillingEngine.Repository.SubscriptionRepo;
import com.yashika.SubscriptionbillingEngine.Service.SubscriptionService;
import com.yashika.SubscriptionbillingEngine.dto.Subscriptiondto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing/Sub")
public class SubscriptionController
{

    private final SubscriptionService subService;
    private final SubscriptionRepo subRepo;

    public SubscriptionController(SubscriptionService subService,SubscriptionRepo subRepo)
    {
        this.subRepo = subRepo;
        this.subService = subService;
    }

    @PostMapping("/create")
    public Subscription create(@RequestBody Subscriptiondto subdto)
    {
        return subService.create(subdto);
    }

    @GetMapping("/getAll")
    public List<Subscription> getAll()
    {
        return subRepo.findAll();
    }

}
