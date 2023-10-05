package study.developia.batch.jobandstepexecutionlistener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.util.Date;

@Slf4j
public class CustomJobExecutionListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Job is started {}", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long startTime = jobExecution.getStartTime().getTime();
        long endTime = jobExecution.getEndTime().getTime();

        log.info("총 소요시간 : {}", endTime - startTime);
    }
}
