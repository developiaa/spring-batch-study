package study.developia.batch.skip;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SkipConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job skipJob() {
        return jobBuilderFactory.get("skipJob")
                .incrementer(new RunIdIncrementer())
                .start(skipStep())
                .build();
    }

    @Bean
    public Step skipStep() {
        return stepBuilderFactory.get("skipStep")
                .<String, String>chunk(5)
                .reader(new ItemReader<>() {
                    int i = 0;

                    @Override
                    public String read() throws Exception {
                        i++;
                        if (i == 3) {
                            throw new SkippableException("skip");
                        }
                        System.out.println("itmeReader = " + i);
                        return i > 20 ? null : String.valueOf(i);
                    }
                })
                .processor(itemProcess())
                .writer(itemWriter())
                .faultTolerant()
//                .skip(SkippableException.class)
//                .skipLimit(2)
                .skipPolicy(limitCheckingItemSkipPolicy())
                .build();
    }

    @Bean
    public SkipPolicy limitCheckingItemSkipPolicy() {
        Map<Class<? extends Throwable>, Boolean> exceptionClass = new HashMap<>();
        exceptionClass.put(SkippableException.class, true);

        LimitCheckingItemSkipPolicy limitCheckingItemSkipPolicy = new LimitCheckingItemSkipPolicy(3, exceptionClass);
        return limitCheckingItemSkipPolicy;
    }

    @Bean
    public ItemWriter<? super String> itemWriter() {
        return new SkipItemWriter();
    }

    @Bean
    public ItemProcessor<? super String, String> itemProcess() {
        return new SkipItemProcessor();
    }
}
