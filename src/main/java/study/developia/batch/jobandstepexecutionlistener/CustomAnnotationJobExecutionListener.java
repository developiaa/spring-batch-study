package study.developia.batch.jobandstepexecutionlistener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

@Slf4j
public class CustomAnnotationJobExecutionListener {
    @BeforeJob
    public void bJob(JobExecution jobExecution) {
        log.info("Job is started {}", jobExecution.getJobInstance().getJobName());
    }

    @AfterJob
    public void aJob(JobExecution jobExecution) {
        long startTime = jobExecution.getStartTime().getTime();
        long endTime = jobExecution.getEndTime().getTime();

        log.info("총 소요시간 : {}", endTime - startTime);
    }
}
