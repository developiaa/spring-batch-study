package study.developia.batch.flatfile;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustomerFieldSetMapper implements FieldSetMapper<Customer> {
    @Override
    public Customer mapFieldSet(FieldSet fs) throws BindException {
        if (fs == null) {
            return null;
        }

        Customer customer = new Customer();
        customer.setName(fs.readString("name"));
        customer.setAge(fs.readInt("age"));
        customer.setYear(fs.readString("year"));

        return customer;
    }
}
