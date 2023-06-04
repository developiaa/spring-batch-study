package study.developia.batch.flatfile.fixedlengthtokenizer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import study.developia.batch.flatfile.Customer;

import java.util.List;

@RequiredArgsConstructor
//@Configuration
public class FlatFilesFixedLengthConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flatFileFixedLengthJob() {
        return jobBuilderFactory.get("flatFileFixedLengthJob")
                .start(flatFileFixedLengthStep1())
                .next(flatFileFixedLengthStep2()).build();
    }

    @Bean
    public Step flatFileFixedLengthStep1() {
        return stepBuilderFactory.get("flatFileFixedLengthStep1")
                .<Customer, String>chunk(5)
                .reader(itemReader())
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> items) throws Exception {
                        System.out.println("items = " + items);
                    }
                })
                .build();
    }


    @Bean
    public FlatFileItemReader<Customer> itemReader() {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("flatFile")
                .resource(new FileSystemResource("/Users/developia/dev/study/spring-batch-study/src/main/resources/customer.txt"))
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>())
                .targetType(Customer.class)
                .linesToSkip(1)
                .fixedLength()
                .strict(false) // 예외가 발생하더라도 처리를 안하는 경우
                .addColumns(new Range(1, 5))
                .addColumns(new Range(6, 9))
                .addColumns(new Range(10, 11))
                .names("name", "year", "age")
                .build();
    }

    @Bean
    public Step flatFileFixedLengthStep2() {
        return stepBuilderFactory.get("flatFileFixedLengthStep2")
                .tasklet(((contribution, chunkContext) -> RepeatStatus.FINISHED))
                .build();
    }


}
