package study.developia.batch.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class JdbcCursorConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final int chunkSize = 10;
    private final DataSource dataSource;

    @Bean
    public Job jdbcCursorJob() {
        return jobBuilderFactory.get("jdbcCursorJob")
                .start(jdbcStep1())
                .build();
    }

    @Bean
    public Step jdbcStep1() {
        return stepBuilderFactory.get("jdbcStep1")
                .<Customer, Customer>chunk(chunkSize)
                .reader(customJdbcCursorItemReader())
                .writer(customJdbcCursorItemWriter())
                .build()
                ;
    }

    @Bean
    public ItemReader<Customer> customJdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<Customer>()
                .name("jdbcCursorItemReader")
                .fetchSize(chunkSize) // 일반적으로 같은 크기를 넣음
                .sql("select id, firstName, lastName, birthdate from customer where firstName like ? order by lastName, firstName")
                .beanRowMapper(Customer.class)
                .queryArguments("A%")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public ItemWriter<Customer> customJdbcCursorItemWriter() {
        return items -> {
            for (Customer item : items) {
                System.out.println(item.toString());
            }
        };
    }

}
