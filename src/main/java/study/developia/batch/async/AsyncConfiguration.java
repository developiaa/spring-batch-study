package study.developia.batch.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AsyncConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job asyncJob() throws InterruptedException {
        return jobBuilderFactory.get("asyncJob")
                .incrementer(new RunIdIncrementer())
//                .start(syncStep())
                .start(asyncStep())
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Step syncStep() throws InterruptedException {
        return stepBuilderFactory.get("syncStep")
                .<Customer, Customer>chunk(100)
                .reader(pagingItemReaderAtAsync())
                .processor(customItemProcessorAtAsync())
                .writer(customItemWriterAtAsync())
                .build();
    }

    @Bean
    public ItemProcessor<Customer, Customer> customItemProcessorAtAsync() throws InterruptedException {
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
    public Step asyncStep() throws InterruptedException {
        return stepBuilderFactory.get("asyncStep")
                .<Customer, Customer>chunk(100)
                .reader(pagingItemReaderAtAsync())
                .processor(asyncItemProcessor())
                .writer(asyncItemWriter())
                .build();
    }

    @Bean
    public AsyncItemWriter asyncItemWriter() {
        AsyncItemWriter<Customer> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(customItemWriterAtAsync());

        return asyncItemWriter;
    }

    @Bean
    public AsyncItemProcessor asyncItemProcessor() throws InterruptedException {
        AsyncItemProcessor<Customer, Customer> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(customItemProcessorAtAsync());
        asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());
//        asyncItemProcessor.afterPropertiesSet(); // bean으로 만들면 필요없음
        return asyncItemProcessor;
    }

    @Bean
    public JdbcPagingItemReader<Customer> pagingItemReaderAtAsync() {
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
    public JdbcBatchItemWriter customItemWriterAtAsync() {
        JdbcBatchItemWriter<Object> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(this.dataSource);
        itemWriter.setSql("insert into customer2 value (:id, :firstname, :lastname, :birthdate)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }
}
