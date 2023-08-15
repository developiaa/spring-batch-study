package study.developia.batch.repeat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.batch.repeat.exception.SimpleLimitExceptionHandler;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class RepeatConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job repeatJob() {
        return jobBuilderFactory.get("repeatJob")
                .incrementer(new RunIdIncrementer())
                .start(repeatStep1())
                .build();
    }

    @Bean
    public Step repeatStep1() {
        return stepBuilderFactory.get("repeatStep1")
                .<String, String>chunk(5)
                .reader(new ItemReader<>() {
                    int i = 0;

                    @Override
                    public String read() throws Exception {
                        i++;
                        return i > 3 ? null : "item" + i;
                    }
                })
                .processor(new ItemProcessor<String, String>() {
                    RepeatTemplate repeatTemplate = new RepeatTemplate();

                    @Override
                    public String process(String item) throws Exception {
//                        repeatTemplate.setCompletionPolicy(new SimpleCompletionPolicy(3));
//                        repeatTemplate.setCompletionPolicy(new TimeoutTerminationPolicy(3000));

                        // 여러 정책 추가 (or 조건임)
//                        CompositeCompletionPolicy compositeCompletionPolicy = new CompositeCompletionPolicy();
//                        CompletionPolicy[] completionPolicies = {
//                                new SimpleCompletionPolicy(3),
//                                new TimeoutTerminationPolicy(1000)
//                        };
//                        compositeCompletionPolicy.setPolicies(completionPolicies);
//                        repeatTemplate.setCompletionPolicy(compositeCompletionPolicy);
//
                        repeatTemplate.setExceptionHandler(simpleLimitExceptionHandler());

                        repeatTemplate.iterate(new RepeatCallback() {
                            @Override
                            public RepeatStatus doInIteration(RepeatContext repeatContext) throws Exception {
                                // 비즈니스 로직
                                log.info("repeatTemplate is testing");
                                throw new RuntimeException("Exception is occurred");
//                                return RepeatStatus.CONTINUABLE;
                            }
                        });

                        return item;
                    }
                })
                .writer(items -> log.info("items {}", items))
                .build();
    }

    @Bean
    public ExceptionHandler simpleLimitExceptionHandler() {
        return new SimpleLimitExceptionHandler(3);
    }

}
