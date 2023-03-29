package com.amigoscode.customer;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    @Override
    public List<Customer> selectAllCustomers() {
        return jdbcTemplate.query("SELECT id,name,email,age, gender FROM customer", customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        var sql = """
                SELECT id,name,email,age,gender
                FROM customer
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, customerRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer (name, email, age,gender)
                VALUES (?,?,?,?)
                """;

        jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                customer.getGender().name()
        );
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql = """
                SELECT count(*)
                FROM customer
                WHERE email = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        var sql = """
                SELECT count(*)
                FROM customer
                WHERE id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomerById(Integer id) {
        jdbcTemplate.update("DELETE FROM customer WHERE id = ?", id);
    }

    @Override
    public void updateCustomer(Customer update) {
        if (update.getName() != null) {
            jdbcTemplate.update("UPDATE customer SET name=? WHERE id=?;",
                    update.getName(),
                    update.getId()
            );
        }
        if (update.getEmail() != null) {
            jdbcTemplate.update("UPDATE customer SET email=? WHERE id=?;",
                    update.getEmail(),
                    update.getId()
            );
        }
        if (update.getAge() != null) {
            jdbcTemplate.update("UPDATE customer SET age=? WHERE id=?;",
                    update.getAge(),
                    update.getId()
            );
        }
        if (update.getGender() != null) {
            jdbcTemplate.update("UPDATE customer SET gender=? WHERE id=?;",
                    update.getGender().name(),
                    update.getId()
            );
        }
    }
}
