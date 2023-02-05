package study.developia.batch.incrementer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
//@Configuration
public class IncrementerConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job incrementerJob() {
        return this.jobBuilderFactory.get("incrementerJob")
                .start(incrementerStep1())
                .next(incrementerStep2())
                .incrementer(new CustomJobParametersIncrementer())
//                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step incrementerStep1() {
        return stepBuilderFactory.get("incrementerStep1")
                .tasklet((contribution, chunkContext) -> {
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step incrementerStep2() {
        return stepBuilderFactory.get("incrementerStep2")
                .tasklet((contribution, chunkContext) -> {
                    return RepeatStatus.FINISHED;
                })
                .build();

    }
}
