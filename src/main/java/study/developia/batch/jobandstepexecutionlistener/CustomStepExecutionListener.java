package study.developia.batch.jobandstepexecutionlistener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomStepExecutionListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        stepExecution.getExecutionContext().put("name", "user1");
        log.info("stepName : {}", stepName);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExitStatus exitStatus = stepExecution.getExitStatus();
        BatchStatus status = stepExecution.getStatus();
        log.info("exitStatus : {}, status : {}", exitStatus, status);
        String name = (String)stepExecution.getExecutionContext().get("name");
        log.info("name {}", name);
        return ExitStatus.COMPLETED;
    }
}
