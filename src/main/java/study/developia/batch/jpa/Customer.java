package study.developia.batch.jpa;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private int age;

    @OneToOne(mappedBy = "customer")
    private Address address;
}
