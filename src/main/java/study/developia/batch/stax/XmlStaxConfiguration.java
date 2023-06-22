package study.developia.batch.stax;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import study.developia.batch.jdbc.Customer;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class XmlStaxConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job xmlStaxEventItemWriterJob() {
        return jobBuilderFactory.get("xmlStaxEventItemWriterJob")
                .incrementer(new RunIdIncrementer())
                .start(xmlStaxEventItemWriterStep())
                .build();
    }

    @Bean
    public Step xmlStaxEventItemWriterStep() {
        return stepBuilderFactory.get("xmlStaxEventItemWriterStep")
                .<Customer, Customer>chunk(3)
                .reader(xmlStaxEventItemReader())
                .writer(xmlStaxEventItemWriter())
                .build()
                ;
    }

    @Bean
    public JdbcPagingItemReader<Customer> xmlStaxEventItemReader() {

        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(this.dataSource);
        reader.setFetchSize(10);
        reader.setRowMapper(new CustomerRowMapper());

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, firstName, lastName, birthdate");
        queryProvider.setFromClause("from customer");
        queryProvider.setWhereClause("where firstname like :firstname");

        Map<String, Order> sortKeys = new HashMap<>(1);

        sortKeys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);
        reader.setQueryProvider(queryProvider);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("firstname", "A%");

        reader.setParameterValues(parameters);

        return reader;
    }

    @Bean
    public StaxEventItemWriter xmlStaxEventItemWriter() {
        return new StaxEventItemWriterBuilder<Customer>()
                .name("customersWriter")
                .marshaller(itemMarshaller())
                .resource(new FileSystemResource("/Users/developia/dev/study/spring-batch-study/src/main/resources/customer2.xml"))
                .rootTagName("customer")
                .overwriteOutput(true)
                .build();

    }

    @Bean
    public Marshaller itemMarshaller() {
        Map<String, Class<?>> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);
        aliases.put("id", Long.class);
        aliases.put("firstname", String.class);
        aliases.put("lastname", String.class);
        aliases.put("birthdate", String.class);

        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        xStreamMarshaller.setAliases(aliases);

        return xStreamMarshaller;
    }

}
