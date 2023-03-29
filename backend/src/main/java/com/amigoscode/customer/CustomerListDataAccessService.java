package com.amigoscode.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO {

    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>(List.of(
                new Customer(1, "alex", "alex@gmail.com", 21, Gender.MALE),
                new Customer(2, "jamila", "jamila@gmail.com", 19, Gender.FEMALE)
        ));
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        return customers.stream()
                .anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customers.removeIf(c -> c.getId().equals(id));
    }

    @Override
    public void updateCustomer(Customer update) {
        customers.add(update); // this will add a second customer to the list - Wrong implementation
    }
}
