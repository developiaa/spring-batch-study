package study.developia.batch.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.developia.batch.jdbc.Customer;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class JpaCursorConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final int chunkSize = 5;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job jpaCursorJob() {
        return jobBuilderFactory.get("jpaCursorJob")
                .start(jpaStep1())
                .build();
    }

    @Bean
    public Step jpaStep1() {
        return stepBuilderFactory.get("jpaStep1")
                .<Customer, Customer>chunk(chunkSize)
                .reader(customJpaCursorItemReader())
                .writer(customJpaCursorItemWriter())
                .build()
                ;
    }

    @Bean
    public ItemReader<Customer> customJpaCursorItemReader() {
        Map<String, Object> paramters = new HashMap<>();
        paramters.put("firstname", "A%");

        return new JpaCursorItemReaderBuilder<Customer>()
                .name("jdbcCursorItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select c from Customer c where firstname like :firstname")
                .parameterValues(paramters)
                .build();
    }

    @Bean
    public ItemWriter<Customer> customJpaCursorItemWriter() {
        return items -> {
            for (Customer item : items) {
                System.out.println(item.toString());
            }
        };
    }

}
