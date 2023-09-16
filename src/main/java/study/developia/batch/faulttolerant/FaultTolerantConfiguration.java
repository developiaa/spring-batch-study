package study.developia.batch.faulttolerant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class FaultTolerantConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job faultTolerantJob() {
        return jobBuilderFactory.get("faultTolerantJob")
                .incrementer(new RunIdIncrementer())
                .start(faultTolerantStep())
                .build();
    }

    @Bean
    public Step faultTolerantStep() {
        return stepBuilderFactory.get("faultTolerantStep")
                .<String, String>chunk(5)
                .reader(new ItemReader<>() {
                    int i = 0;

                    @Override
                    public String read() throws Exception {
                        i++;
                        if (i == 1) {
                            throw new IllegalArgumentException("skipped");
                        }

                        return i > 3 ? null : "item" + i;
                    }
                })
                .processor(new ItemProcessor<String, String>() {
                    RepeatTemplate repeatTemplate = new RepeatTemplate();

                    @Override
                    public String process(String item) throws Exception {
                        throw new IllegalStateException("retry");
//                        return null;
                    }
                })
                .writer(items -> log.info("items {}", items))
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skipLimit(2)
                .retry(IllegalStateException.class)
                .retryLimit(2)
                .build();
    }
}
