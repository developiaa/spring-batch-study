package study.developia.batch.simpleflow;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@RequiredArgsConstructor
@Configuration
public class SimpleFlowConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("simpleFlow")
                .start(simpleFlow())
                .next(simpleStep103())
                .end() // 여기까지 simpleFlow 객체 생성
                .build();
    }

    @Bean
    public Flow simpleFlow() {
        FlowBuilder<Flow> builder = new FlowBuilder<>("flow");
        builder.start(simpleStep101())
                .next(simpleStep102())
                .end();
        return builder.build();
    }

    @Bean
    public Step simpleStep101() {
        return stepBuilderFactory.get("simpleStep101")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("simpleStep101 has executed");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step simpleStep102() {
        return stepBuilderFactory.get("simpleStep102")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("simpleStep102 has executed");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step simpleStep103() {
        return stepBuilderFactory.get("simpleStep103")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("simpleStep103 has executed");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
