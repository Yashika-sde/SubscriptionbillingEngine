package com.yashika.SubscriptionbillingEngine.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class MrrReport {
    private BigDecimal mrr;
    private long activeSubscriptions;
    private Map<String, Long> subscriptionsPerPlan;
}