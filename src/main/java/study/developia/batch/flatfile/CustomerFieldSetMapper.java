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
        customer.setName(fs.readString(0));
        customer.setAge(fs.readInt(1));
        customer.setYear(fs.readString(2));

        return customer;
    }
}
