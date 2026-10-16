package com.yashika.SubscriptionbillingEngine.Service;

import com.yashika.SubscriptionbillingEngine.Entity.Enum.Status;
import com.yashika.SubscriptionbillingEngine.Entity.Plan;
import com.yashika.SubscriptionbillingEngine.Entity.Subscription;
import com.yashika.SubscriptionbillingEngine.Repository.SubscriptionRepo;
import com.yashika.SubscriptionbillingEngine.dto.MrrReport;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final SubscriptionRepo subscriptionRepo;

    public ReportService(SubscriptionRepo subscriptionRepo) {
        this.subscriptionRepo = subscriptionRepo;
    }

    public MrrReport mrr() {
        List<Subscription> active = subscriptionRepo.findByStatus(Status.ACTIVE);

        BigDecimal total = BigDecimal.ZERO;
        Map<String, Long> perPlan = new HashMap<>();

        for (Subscription s : active) {
            Plan plan = s.getPlan();
            total = total.add(toMonthly(plan));
            perPlan.merge(plan.getName(), 1L, Long::sum);
        }
        return new MrrReport(total, active.size(), perPlan);
    }

    private BigDecimal toMonthly(Plan plan) {
        return switch (plan.getBillingCycle()) {
            case MONTHLY -> plan.getPrice();
            case QUARTELY -> plan.getPrice().divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
            case YEARLY -> plan.getPrice().divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
        };
    }
}