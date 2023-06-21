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
public class FlatFilesFormattedConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flatFilesFormattedJob() {
        return jobBuilderFactory.get("flatFilesFormattedJob")
                .incrementer(new RunIdIncrementer())
                .start(flatFilesFormattedStep())
                .build();
    }

    @Bean
    public Step flatFilesFormattedStep() {
        return stepBuilderFactory.get("flatFilesFormattedStep")
                .<Customer, Customer>chunk(10)
                .reader(flatFilesFormattedReader())
                .writer(flatFilesFormattedWriter())
                .build();
    }

    @Bean
    public ItemWriter<? super Customer> flatFilesFormattedWriter() {
        return new FlatFileItemWriterBuilder<>()
                .name("flatFileWriter")
                .resource(new FileSystemResource("/Users/developia/dev/study/spring-batch-study/src/main/resources/customer3.txt"))
                .append(true)
                .formatted()
                .format("%-2d%-15s%-2d")
                .names(new String[]{"id", "name", "age"})
                .build();
    }

    @Bean
    public ItemReader<? extends Customer> flatFilesFormattedReader() {
        List<Customer> customers = Arrays.asList(
                new Customer(1, "hong gil dong", 41),
                new Customer(2, "hong gil dong", 42),
                new Customer(3, "hong gil dong", 43)
        );
        ListItemReader<Customer> reader = new ListItemReader<>(customers);
        return reader;
    }
}
