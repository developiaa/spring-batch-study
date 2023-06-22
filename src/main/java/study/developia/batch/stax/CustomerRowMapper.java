package study.developia.batch.stax;

import org.springframework.jdbc.core.RowMapper;
import study.developia.batch.jdbc.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;


public class CustomerRowMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Customer(rs.getLong("id"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("birthdate")
        );
    }

}
