package study.developia.batch.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class MultiThreadStepConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job multiThreadJob() throws InterruptedException {
        return jobBuilderFactory.get("multiThreadJob")
                .incrementer(new RunIdIncrementer())
                .start(multiThreadStep())
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Step multiThreadStep() throws InterruptedException {
        return stepBuilderFactory.get("multiThreadStep")
                .<Customer, Customer>chunk(100)
                .reader(pagingItemReaderAtMultiThread())
                .listener(new CustomItemReadListener())
                .processor((ItemProcessor<Customer, Customer>) item->item)
                .listener(new CustomItemProcessorListener())
                .writer(customItemWriterAtMultiThread())
                .listener(new CustomItemWriterListener())
                .taskExecutor(taskExecutor()) // 작성하지 않으면 동기로 작동
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setThreadNamePrefix("async-thread");

        return taskExecutor;
    }

    @Bean
    public ItemProcessor<Customer, Customer> customItemProcessorAtMultiThread() throws InterruptedException {
        return new ItemProcessor<Customer, Customer>() {
            @Override
            public Customer process(Customer item) throws Exception {
                Thread.sleep(10);
                return new Customer(item.getId(), item.getFirstname().toUpperCase(),
                        item.getLastname().toUpperCase(), item.getBirthdate());
            }
        };
    }



    @Bean
    public JdbcBatchItemWriter customItemWriterAtAsync() {
        JdbcBatchItemWriter<Object> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("insert into customer2 value (:id, :firstname, :lastname, :birthdate)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean
    public JdbcPagingItemReader<Customer> pagingItemReaderAtMultiThread() {
        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(this.dataSource);
        reader.setFetchSize(300);
        reader.setRowMapper(new CustomerRowMapper());

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, firstName, lastName, birthdate");
        queryProvider.setFromClause("from customer");

        Map<String, Order> sortKeys = new HashMap<>(1);

        sortKeys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);
        reader.setQueryProvider(queryProvider);

        return reader;
    }

    @Bean
    public JdbcBatchItemWriter customItemWriterAtMultiThread() {
        JdbcBatchItemWriter<Object> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("insert into customer2 value (:id, :firstname, :lastname, :birthdate)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }
}
