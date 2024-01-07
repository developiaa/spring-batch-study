package study.developia.batch.example.scheduler;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ApiJobRunner extends JobRunner {
    @Autowired
    private Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {
        // job에 대한 정보
        JobDetail jobDetail = buildJobDetail(ApiSchJob.class, "apiJob", "batch", new HashMap<>());
        // 언제 스케줄러가 실행할 것인지에 대한 정보
        Trigger trigger = buildJobTrigger("0/30 * * * * ?");

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
