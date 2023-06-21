package study.developia.batch.delimited;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
public class Customer {
    private long id;
    private String name;
    private int age;
}
