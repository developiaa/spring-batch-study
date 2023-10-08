package study.developia.batch.test;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Date;
import java.util.List;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes = {TestConfiguration.class, TestBatchConfig.class})
public class TestConfigurationTest {
    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void simpleJobTest() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "user1")
                .addLong("date", new Date().getTime())
                .toJobParameters();
        // when
//        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        JobExecution jobExecution1 = jobLauncherTestUtils.launchStep("testStep");

        // then
//        Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
//        Assert.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);

        StepExecution stepExecution = (StepExecution) ((List) jobExecution1.getStepExecutions()).get(0);
        Assert.assertEquals(stepExecution.getCommitCount(), 10);
        Assert.assertEquals(stepExecution.getReadCount(), 950);
        Assert.assertEquals(stepExecution.getWriteCount(), 950);

    }

    @AfterAll
    public void clear() {
        jdbcTemplate.execute("delete from customer2");
    }
}
