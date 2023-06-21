package study.developia.batch.delimited;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class FlatFilesDelimitedConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flatFilesDelimitedJob() {
        return jobBuilderFactory.get("flatFilesDelimitedJob")
                .incrementer(new RunIdIncrementer())
                .start(flatFilesDelimitedStep())
                .build();
    }

    @Bean
    public Step flatFilesDelimitedStep() {
        return stepBuilderFactory.get("flatFilesDelimitedStep")
                .<Customer, Customer>chunk(10)
                .reader(flatFilesDelimitedReader())
                .writer(flatFilesDelimitedWriter())
                .build();
    }

    @Bean
    public ItemWriter<? super Customer> flatFilesDelimitedWriter() {
        return new FlatFileItemWriterBuilder<>()
                .name("flatFileWriter")
                .resource(new FileSystemResource("/Users/developia/dev/study/spring-batch-study/src/main/resources/customer2.txt"))
                .append(true)
                .delimited()
                .delimiter("|")
                .names(new String[]{"id", "name", "age"})
                .build();
    }

    @Bean
    public ItemReader<? extends Customer> flatFilesDelimitedReader() {
        List<Customer> customers = Arrays.asList(
                new Customer(1, "hong gil dong", 41),
                new Customer(2, "hong gil dong", 42),
                new Customer(3, "hong gil dong", 43)
        );
        ListItemReader<Customer> reader = new ListItemReader<>(customers);
        return reader;
    }
}
