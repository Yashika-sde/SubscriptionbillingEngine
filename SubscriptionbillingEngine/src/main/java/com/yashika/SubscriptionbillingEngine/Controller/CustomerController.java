package com.yashika.SubscriptionbillingEngine.Controller;

import com.yashika.SubscriptionbillingEngine.Entity.Customer;
import com.yashika.SubscriptionbillingEngine.Repository.CustomerRepo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/billing/Customer")
public class CustomerController
{
    private final CustomerRepo customerRepo;

    public CustomerController(CustomerRepo customerRepo)
    {
        this.customerRepo = customerRepo;
    }

    @RequestMapping("/create")
    public Customer create(@RequestBody Customer customer)
    {
        return customerRepo.save(customer);
    }

    @RequestMapping("/read")
    public List<Customer> getAll()
    {
        return customerRepo.findAll();
    }
}
