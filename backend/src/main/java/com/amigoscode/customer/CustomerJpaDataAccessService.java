package com.amigoscode.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository("jpa")
public class CustomerJpaDataAccessService implements CustomerDAO {

    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        return customerRepository.existsById(id);
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void updateCustomer(Customer update) {
        customerRepository.save(update);
    }
}
