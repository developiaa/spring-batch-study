package study.developia.batch.task;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
//@Configuration
public class TaskStepConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job chunkStepJob() {
        return jobBuilderFactory.get("chunkStepJob")
                .start(chunkStep())
                .next(stepTest())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step taskStep() {
        return stepBuilderFactory.get("taskStep")
                .tasklet((contribution, chunkContext) -> {
                    return RepeatStatus.FINISHED;
                })
                .allowStartIfComplete(true) // 성공 유무와 상관 없이 다시 실행함
                .build();
    }

    @Bean
    public Step chunkStep() {
        return stepBuilderFactory.get("chunkStep")
                .<String, String>chunk(10)
                .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3", "item4")))
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String item) throws Exception {
                        return item.toUpperCase();
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> items) throws Exception {
                        items.forEach(item -> System.out.println(item));
                    }
                })
                .build();
    }

    @Bean
    public Step stepTest() {
        return stepBuilderFactory.get("stepTest")
                .tasklet(new CustomTasklet())
                .startLimit(3) // 최대 3번 실행, 4번째부터는 step exectuion은 생성되지 않음
                .build();
    }

}
