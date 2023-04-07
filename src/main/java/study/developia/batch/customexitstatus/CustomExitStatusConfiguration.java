package study.developia.batch.customexitstatus;

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
@Configuration
public class CustomExitStatusConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job exitStatusJob() {
        return this.jobBuilderFactory.get("exitStatusJob")
                .start(eStep1())
                .on("FAILED")
                .to(eStep2()) // step2가 실패했을 때 어떤 조건 값이 없으면 배치의 job execution은 자동으로 failed 처리
                .on("PASS")
                .stop()
                .end()
                .build();
    }

    @Bean
    public Step eStep1() {
        return stepBuilderFactory.get("e step1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step 1 has executed");
                    contribution.getStepExecution().setExitStatus(ExitStatus.FAILED); // 처음 flow 실행됨
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step eStep2() {
        return stepBuilderFactory.get("e step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step 2 has executed");
                    return RepeatStatus.FINISHED;
                })
                .listener(new PassCheckingListener())
                .build();
    }

}
