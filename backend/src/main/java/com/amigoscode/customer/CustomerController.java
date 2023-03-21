package com.amigoscode.customer;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RequestMapping("api/v1/customers")
@RestController
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{customerId}")
    public Customer getCustomer(@PathVariable Integer customerId) {
        return customerService.getCustomer(customerId);
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        customerService.addCustomer(customerRegistrationRequest);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable Integer customerId) {
        customerService.deleteCustomerById(customerId);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(
            @PathVariable Integer customerId,
            @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        customerService.updateCustomer(customerId, customerUpdateRequest);
    }
}
