package study.developia.batch.flowjob;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FlowJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flowJob() {
        return jobBuilderFactory.get("flowJob")
                .start(step101())
                .on("COMPLETED").to(step103())
                .from(step101())
                .on("FAILED").to(step102())
                .end() // 성공하면 step101, 실패시 step102
                .build();
    }

    @Bean
    public Step step101() {
        return stepBuilderFactory.get("step101")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step101 has executed");
                        throw new RuntimeException("step101 was failed");
//                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step step102() {
        return stepBuilderFactory.get("step102")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step102 has executed");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step step103() {
        return stepBuilderFactory.get("step103")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step103 has executed");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }
}
