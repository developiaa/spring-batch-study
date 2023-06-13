package study.developia.batch.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.developia.batch.jdbc.Customer;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class JpaPagingConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final int chunkSize = 5;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job jpaPagingJob() {
        return jobBuilderFactory.get("jpaPagingJob")
                .start(jpaPagingStep1())
                .build();
    }

    @Bean
    public Step jpaPagingStep1() {
        return stepBuilderFactory.get("jpaPagingStep1")
                .<Customer, Customer>chunk(chunkSize)
                .reader(customJpaPagingItemReader())
                .writer(customJpaPagingItemWriter())
                .build();
    }

    @Bean
    public ItemReader<Customer> customJpaPagingItemReader() {
        Map<String, Object> paramters = new HashMap<>();
        paramters.put("firstname", "A%");

        return new JpaPagingItemReaderBuilder<Customer>()
                .name("customJpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("select c from Customer c join fetch c.address")
                .parameterValues(paramters)
                .build();
    }

    @Bean
    public ItemWriter<Customer> customJpaPagingItemWriter() {
        return items -> {
            for (Customer item : items) {
                System.out.println(item.toString());
            }
        };
    }

}
