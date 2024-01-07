package study.developia.batch.example.scheduler;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class FileJobRunner extends JobRunner {
    @Autowired
    private Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {
        String[] sourceArgs = args.getSourceArgs();

        // job에 대한 정보
        JobDetail jobDetail = buildJobDetail(FileSchJob.class, "fileJob", "batch", new HashMap<>());
        // 언제 스케줄러가 실행할 것인지에 대한 정보
        Trigger trigger = buildJobTrigger("0/50 * * * * ?");

        jobDetail.getJobDataMap().put("requestDate", sourceArgs[0]);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


}
