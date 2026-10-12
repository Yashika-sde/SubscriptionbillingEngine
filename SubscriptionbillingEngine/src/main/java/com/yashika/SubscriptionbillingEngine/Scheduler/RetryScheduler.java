package com.yashika.SubscriptionbillingEngine.Scheduler;

import com.yashika.SubscriptionbillingEngine.Entity.Enum.InvoiceStatus;
import com.yashika.SubscriptionbillingEngine.Entity.Invoice;
import com.yashika.SubscriptionbillingEngine.Repository.InvoiceRepo;
import com.yashika.SubscriptionbillingEngine.Service.PaymentService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RetryScheduler
{
      private static final int MAX_RETRIES = 3;

      private final InvoiceRepo invoiceRepo;
      private final PaymentService paymentService;

      public RetryScheduler(InvoiceRepo invoiceRepo,PaymentService paymentService)
      {
          this.invoiceRepo = invoiceRepo;
          this.paymentService = paymentService;
      }

    @Scheduled(cron = "0 0 2 * * *")
      public void retryFailedPayments()
      {
          List<Invoice> failed = invoiceRepo.findByStatusAndRetryCountLessThan(InvoiceStatus.FAILED,MAX_RETRIES);

          System.out.println("Retry ran, failed invoices: " + failed.size());

          for (Invoice inv : failed) {
              try {
                  paymentService.charge(inv);
              } catch (Exception e) {
                  System.out.println("Retry failed for invoice " + inv.getId() + ": " + e.getMessage());
              }
          }
      }
}
