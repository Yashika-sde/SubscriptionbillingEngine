package com.yashika.SubscriptionbillingEngine.Controller;

import com.yashika.SubscriptionbillingEngine.Service.ReportService;
import com.yashika.SubscriptionbillingEngine.dto.MrrReport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/billing/reports")
public class ReportController {


    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/mrr")
    public MrrReport mrr() {
        return reportService.mrr();
    }
}
