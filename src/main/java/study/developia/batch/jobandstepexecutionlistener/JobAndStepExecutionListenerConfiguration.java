package study.developia.batch.jobandstepexecutionlistener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class JobAndStepExecutionListenerConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CustomStepExecutionListener customStepExecutionListener;

    @Bean
    public Job jobAndStepExecutionListenerJob() {
        return jobBuilderFactory.get("jobAndStepExecutionListenerJob")
                .incrementer(new RunIdIncrementer())
                .start(jobAndStepExecutionListenerStep())
                .next(jobAndStepExecutionListenerStep2())
                .listener(new CustomJobExecutionListener())
//                .listener(new CustomAnnotationJobExecutionListener())
                .build();
    }


    @Bean
    public Step jobAndStepExecutionListenerStep() {
        return stepBuilderFactory.get("jobAndStepExecutionListenerJob")
                .tasklet(((contribution, chunkContext) -> RepeatStatus.FINISHED))
                .listener(customStepExecutionListener)
                .build();
    }

    @Bean
    public Step jobAndStepExecutionListenerStep2() {
        return stepBuilderFactory.get("jobAndStepExecutionListenerJob2")
                .tasklet(((contribution, chunkContext) -> RepeatStatus.FINISHED))
                .listener(customStepExecutionListener)
                .build();
    }

}
