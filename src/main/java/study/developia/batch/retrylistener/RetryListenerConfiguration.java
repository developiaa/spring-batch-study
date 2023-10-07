package study.developia.batch.retrylistener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.developia.batch.skiplistener.LinkedListItemReader;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RetryListenerConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job retryListenerJob() {
        return jobBuilderFactory.get("retryListenerJob")
                .incrementer(new RunIdIncrementer())
                .start(retryListenerStep())
                .build();
    }


    @Bean
    public Step retryListenerStep() {
        return stepBuilderFactory.get("retryListenerStep")
                .<Integer, String>chunk(10)
                .reader(retryListItemReader())
                .processor(new CustomItemProcessor())
                .writer(new CustomItemWriter())
                .faultTolerant()
                .retry(CustomRetryException.class)
                .retryLimit(2)
                .listener(new CustomRetryListener())
                .build();
    }

    @Bean
    public ItemReader<Integer> retryListItemReader() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        return new RetryLinkedListItemReader<>(list);
    }

}
