package study.developia.batch.synchronizeditemstream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class NotSynchronizedConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;


    @Bean
    public Job notSynchronizedJob() {
        return jobBuilderFactory.get("notSynchronizedJob")
                .incrementer(new RunIdIncrementer())
                .start(notSynchronizedStep())
                .build();
    }

    @Bean
    public Step notSynchronizedStep() {
        return stepBuilderFactory.get("notSynchronizedStep")
                .<Customer, Customer>chunk(60)
                .reader(notSafetyJdbcCursorItemReader())
                .listener(new ItemReadListener<Customer>() {
                    @Override
                    public void beforeRead() {

                    }

                    @Override
                    public void afterRead(Customer item) {
                        log.info("Thread : {} item.getId() {}", Thread.currentThread().getName(), item.getId());
                    }

                    @Override
                    public void onReadError(Exception ex) {

                    }
                })
                .writer(notSafetyJdbcBatchItemWriter())
                .taskExecutor(notSafetyTaskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<Customer> notSafetyJdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<Customer>()
                .fetchSize(60) // 일반적으로 같은 크기를 넣음
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Customer.class))
                .sql("select id, firstName, lastName, birthdate from customer")
                .name("NotSafetyReader")
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Customer> notSafetyJdbcBatchItemWriter() {
        JdbcBatchItemWriter<Customer> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("insert into customer2 values (:id, :firstName, :lastName, :birthdate)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    @Bean
    public TaskExecutor notSafetyTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setThreadNamePrefix("not-safety-thread-");
        return taskExecutor;
    }


}
