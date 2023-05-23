package study.developia.batch.chunk;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@RequiredArgsConstructor
//@Configuration
public class ItemReaderProcessorWriterConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job chunkJob() {
        return jobBuilderFactory.get("chunkJob2").start(chunkStep3()).next(chunkStep4()).build();
    }

    @Bean
    public Step chunkStep3() {
        return stepBuilderFactory.get("chunkStep3")
                .<Customer, Customer>chunk(3)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ItemWriter<? super Customer> itemWriter() {
        return new CustomItemWriter();
    }

    @Bean
    public ItemProcessor<? super Customer, ? extends Customer> itemProcessor() {
        return new CustomItemProcessor();
    }


    @Bean
    public ItemReader<Customer> itemReader() {
        return new CustomItemReader(
                Arrays.asList(
                        new Customer("user1"),
                        new Customer("user2"),
                        new Customer("user3")
                ));
    }

    @Bean
    public Step chunkStep4() {
        return stepBuilderFactory.get("chunkStep4").tasklet((contribution, chunkContext) -> {
            System.out.println("chunkStep4 has executed");
            return RepeatStatus.FINISHED;
        }).build();
    }
}
