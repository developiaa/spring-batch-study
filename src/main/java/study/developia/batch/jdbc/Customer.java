package study.developia.batch.jdbc;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;
    private String birthdate;
}
