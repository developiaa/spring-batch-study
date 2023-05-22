package study.developia.batch.chunk;


import org.springframework.batch.item.*;

public class CustomItemProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {
        customer.setName(customer.getName().toUpperCase());
        return customer;
    }
}
