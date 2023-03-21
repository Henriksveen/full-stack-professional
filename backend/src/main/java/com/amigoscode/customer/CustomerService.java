package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jpa") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDAO.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("customer with id [%s] not found".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if (customerDAO.existsPersonWithEmail(customerRegistrationRequest.email()))
            throw new DuplicateResourceException("email already taken");

        customerDAO.insertCustomer(
                new Customer(
                        customerRegistrationRequest.name(),
                        customerRegistrationRequest.email(),
                        customerRegistrationRequest.age()
                )
        );
    }

    public void deleteCustomerById(Integer customerId) {
        if (!customerDAO.existsPersonWithId(customerId))
            throw new ResourceNotFoundException("customer with id [%s] not found".formatted(customerId));

        customerDAO.deleteCustomerById(customerId);
    }

    public void updateCustomer(Integer id, CustomerUpdateRequest updatedRequest) {
        Customer customer = getCustomer(id);
        boolean changes = false;

        if (updatedRequest.name() != null && !customer.getName().equals(updatedRequest.name())) {
            customer.setName(updatedRequest.name());
            changes = true;
        }
        if (updatedRequest.email() != null && !customer.getEmail().equals(updatedRequest.email())) {
            if (customerDAO.existsPersonWithEmail(updatedRequest.email()))
                throw new DuplicateResourceException("email already taken");
            customer.setEmail(updatedRequest.email());
            changes = true;
        }
        if (updatedRequest.age() != null && !customer.getAge().equals(updatedRequest.age())) {
            customer.setAge(updatedRequest.age());
            changes = true;
        }
        if (!changes)
            throw new RequestValidationException("no data changes found");

        customerDAO.updateCustomer(customer);
    }
}
