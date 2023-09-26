package study.developia.batch.async.parallel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import study.developia.batch.async.StopWatchJobListener;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class ParallelStepConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job parallelStepJob() {
        return jobBuilderFactory.get("parallelStepJob")
                .incrementer(new RunIdIncrementer())
                .start(parallelFlow())
                .split(taskExecutor()).add(parallelFlow2())
                .end()
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Flow parallelFlow() {
        TaskletStep parallelStep = stepBuilderFactory.get("parallelStep")
                .tasklet(parallelTasklet())
                .build();

        return new FlowBuilder<Flow>("parallelFlow")
                .start(parallelStep)
                .build();
    }

    @Bean
    public Flow parallelFlow2() {
        TaskletStep parallelStep2 = stepBuilderFactory.get("parallelStep2")
                .tasklet(parallelTasklet())
                .build();

        TaskletStep parallelStep3 = stepBuilderFactory.get("parallelStep3")
                .tasklet(parallelTasklet())
                .build();

        return new FlowBuilder<Flow>("parallelFlow2")
                .start(parallelStep2)
                .next(parallelStep3)
                .build();
    }

    @Bean
    public Tasklet parallelTasklet() {
        return new ParallelTasklet();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setThreadNamePrefix("async-thread-");
        return taskExecutor;
    }

}
