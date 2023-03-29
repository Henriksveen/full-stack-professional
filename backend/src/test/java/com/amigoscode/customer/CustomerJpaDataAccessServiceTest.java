package com.amigoscode.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerJpaDataAccessServiceTest {

    private CustomerJpaDataAccessService underTest;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        underTest = new CustomerJpaDataAccessService(customerRepository);
    }

    @Test
    void selectAllCustomers() {
        // When
        underTest.selectAllCustomers();

        // Then
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        // Given
        int id = 1;

        // When
        underTest.selectCustomerById(id);

        // Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = new Customer(1, "foo", "foo@gmail.com", 2, Gender.MALE);

        // When
        underTest.insertCustomer(customer);

        // Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        String email = "foo@gmail.com";

        // When
        underTest.existsPersonWithEmail(email);

        // Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsPersonWithId() {
        // Given
        int id = 1;

        // When
        underTest.existsPersonWithId(id);

        // Then
        verify(customerRepository).existsById(id);
    }

    @Test
    void deleteCustomerById() {
        // Given
        int id = 1;

        // When
        underTest.deleteCustomerById(id);

        // Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer update = new Customer(1, "foo", "foo@gmail.com", 2, Gender.MALE);

        // When
        underTest.updateCustomer(update);

        // Then
        verify(customerRepository).save(update);
    }
}
