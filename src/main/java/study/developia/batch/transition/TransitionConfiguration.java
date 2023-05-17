package study.developia.batch.transition;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
//@Configuration
public class TransitionConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job transitionJob() {
        return this.jobBuilderFactory.get("transitionJob")
                .start(tStep1())
                    .on("FAILED")
                    .to(tStep2())
                    .on("FAILED")
                    .end()
                .from(tStep1())
                    .on("*")
                    .to(tStep3())
                    .next(tStep4())
                .from(tStep2())
                    .on("*")
                    .to(tStep5())
                    .end()
                .build();
    }

    @Bean
    public Step tStep1() {
        return stepBuilderFactory.get("t step1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step 1 has executed");
                    contribution.setExitStatus(ExitStatus.FAILED); // 처음 flow 실행됨
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step tStep2() {
        return stepBuilderFactory.get("t step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step 2 has executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step tStep3() {
        return stepBuilderFactory.get("t step3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step 3 has executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step tStep4() {
        return stepBuilderFactory.get("t step4")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step 4 has executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step tStep5() {
        return stepBuilderFactory.get("t step5")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step 5 has executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
