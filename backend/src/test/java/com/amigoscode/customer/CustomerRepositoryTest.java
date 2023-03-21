package com.amigoscode.customer;

import com.amigoscode.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.save(customer);
        // When
        boolean actual = underTest.existsCustomerByEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existPersonWithEmailReturnsFalseWhenDoesNotExists() {
        // Given
        String email = FAKER.internet().emailAddress() + "-" + UUID.randomUUID();

        // When
        boolean actual = underTest.existsCustomerByEmail(email);

        // Then
        assertThat(actual).isFalse();
    }
}
