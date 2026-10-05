package com.yashika.SubscriptionbillingEngine.Controller;

import com.yashika.SubscriptionbillingEngine.Entity.Plan;
import com.yashika.SubscriptionbillingEngine.Repository.PlanRepo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/billing/Plan")
@RestController
public class PlanController
{
    private final PlanRepo planRepo;

    public PlanController(PlanRepo planRepo)
    {
        this.planRepo = planRepo;
    }

    @RequestMapping("/create")
    public Plan create(@RequestBody Plan plan)
    {
        return planRepo.save(plan);
    }

    @RequestMapping("/read")
    public List<Plan> getAll()
    {
        return planRepo.findAll();
    }
}
