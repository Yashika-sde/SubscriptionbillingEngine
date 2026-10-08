package com.yashika.SubscriptionbillingEngine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class SubscriptionbillingEngineApplication
{

	public static void main(String[] args)
	{

		SpringApplication.run(SubscriptionbillingEngineApplication.class, args);
	}

}
