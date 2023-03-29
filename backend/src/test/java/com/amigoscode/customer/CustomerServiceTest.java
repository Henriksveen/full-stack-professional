package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;

    @Mock
    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
    }

    @Test
    void canGetAllCustomers() {
        // When
        underTest.getAllCustomers();

        // Then
        verify(customerDAO).selectAllCustomers();
    }

    @Test
    void catGetCustomer() {
        // Given
        int id = 1;
        Customer customer = new Customer(id, "foo", "foo@gmail.com", 20, Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        // When
        Customer actual = underTest.getCustomer(id);

        // Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenCustomerReturnEmptyOptional() {
        // Given
        int id = 1;
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {
        // Given
        String email = "foo@gmail.com";
        when(customerDAO.existsPersonWithEmail(email)).thenReturn(false);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("foo", email, 20, Gender.MALE);

        // When
        underTest.addCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());

        Customer captuerdCustomer = customerArgumentCaptor.getValue();

        assertThat(captuerdCustomer.getId()).isNull();
        assertThat(captuerdCustomer.getName()).isEqualTo(request.name());
        assertThat(captuerdCustomer.getEmail()).isEqualTo(request.email());
        assertThat(captuerdCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingACustomer() {
        // Given
        String email = "foo@gmail.com";
        when(customerDAO.existsPersonWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("foo", email, 20, Gender.MALE);

        // When
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(customerDAO, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerById() {
        // Given
        int id = 1;
        when(customerDAO.existsPersonWithId(id)).thenReturn(true);

        // When
        underTest.deleteCustomerById(id);

        // Then
        verify(customerDAO).deleteCustomerById(id);
    }

    @Test
    void willThrowWhenDeleteCustomerByIdNotExists() {
        // Given
        int id = 1;
        when(customerDAO.existsPersonWithId(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

        // Then
        verify(customerDAO, never()).deleteCustomerById(id);

    }

    @Test
    void canUpdateAllCustomersProperties() {
        // Given
        int id = 1;
        Customer customer = new Customer(id, "foo", "foo@gmail.com", 20, Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest request = new CustomerUpdateRequest("update", "update@gmail.com", 30);
        when(customerDAO.existsPersonWithEmail(request.email())).thenReturn(false);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(argumentCaptor.capture());
        Customer capturedCustomer = argumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo("update");
        assertThat(capturedCustomer.getEmail()).isEqualTo("update@gmail.com");
        assertThat(capturedCustomer.getAge()).isEqualTo(30);
    }

    @Test
    void canUpdateOnlyCustomerName() {
        // Given
        int id = 1;
        Customer customer = new Customer(id, "foo", "foo@gmail.com", 20, Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest request = new CustomerUpdateRequest("update", null, null);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(argumentCaptor.capture());
        Customer capturedCustomer = argumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo("update");
        assertThat(capturedCustomer.getEmail()).isEqualTo("foo@gmail.com");
        assertThat(capturedCustomer.getAge()).isEqualTo(20);
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        // Given
        int id = 1;
        Customer customer = new Customer(id, "foo", "foo@gmail.com", 20, Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "update@gmail.com";
        CustomerUpdateRequest request = new CustomerUpdateRequest(null, newEmail, null);
        when(customerDAO.existsPersonWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(argumentCaptor.capture());
        Customer capturedCustomer = argumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo("foo");
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
        assertThat(capturedCustomer.getAge()).isEqualTo(20);
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        // Given
        int id = 1;
        Customer customer = new Customer(id, "foo", "foo@gmail.com", 20, Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest request = new CustomerUpdateRequest(null, null, 30);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(argumentCaptor.capture());
        Customer capturedCustomer = argumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo("foo");
        assertThat(capturedCustomer.getEmail()).isEqualTo("foo@gmail.com");
        assertThat(capturedCustomer.getAge()).isEqualTo(30);
    }


    @Test
    void willThrowWhenUpdateCustomerHasNoChanges() {
        // Given
        int id = 1;
        Customer customer = new Customer(id, "foo", "foo@gmail.com", 20, Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge());

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        // Then
        verify(customerDAO, never()).updateCustomer(any());
    }

    @Test
    void willThrowWhenUpdateCustomerEmailExists() {
        // Given
        int id = 1;
        Customer customer = new Customer(id, "foo", "foo@gmail.com", 20, Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String email = "exists@gmail.com";
        when(customerDAO.existsPersonWithEmail(email)).thenReturn(true);
        CustomerUpdateRequest request = new CustomerUpdateRequest(null, email, null);

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");
        // Then
        verify(customerDAO, never()).updateCustomer(any());
    }
}
