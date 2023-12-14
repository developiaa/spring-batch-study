package study.developia.batch.operation;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
//@Configuration
public class JobOperationConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobRegistry jobRegistry;

    @Bean
    public Job operationJob() {
        return jobBuilderFactory.get("operationJob")
                .incrementer(new RunIdIncrementer())
                .start(operationStep1())
                .next(operationStep2())
                .build();
    }

    @Bean
    public Step operationStep1() {
        return stepBuilderFactory.get("operationStep1")
                .tasklet((contribution, chunkContext) -> {
                            System.out.println("operation step1 was executed");
                            Thread.sleep(5000);
                            return RepeatStatus.FINISHED;
                        }
                )
                .build();
    }

    @Bean
    public Step operationStep2() {
        return stepBuilderFactory.get("operationStep2")
                .tasklet((contribution, chunkContext) -> {
                            System.out.println("operation step2 was executed");
                            Thread.sleep(5000);
                            return RepeatStatus.FINISHED;
                        }
                )
                .build();
    }

    @Bean
    public BeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }
}
