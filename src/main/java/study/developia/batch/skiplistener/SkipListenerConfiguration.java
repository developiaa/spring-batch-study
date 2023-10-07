package study.developia.batch.skiplistener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class SkipListenerConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job skipListenerJob() {
        return jobBuilderFactory.get("skipListenerJob")
                .incrementer(new RunIdIncrementer())
                .start(skipListenerStep())
                .build();
    }


    @Bean
    public Step skipListenerStep() {
        return stepBuilderFactory.get("skipListenerStep")
                .<Integer, String>chunk(10)
                .reader(chunkListItemReader())
                .processor(new ItemProcessor<Integer, String>() {
                    @Override
                    public String process(Integer item) throws Exception {
                        if (item == 4) {
                            throw new CustomSkipException("process skipped");
                        }
                        return "item" + item;
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> items) throws Exception {
                        for (String item : items) {
                            if (item.equals("item5")) {
                                throw new CustomSkipException("write skipped");
                            }
                            log.info("item {}", item);
                        }
                    }
                })
                .faultTolerant()
                .skip(CustomSkipException.class)
                .skipLimit(3)
                .listener(new CustomSkipListener())
                .build();
    }

    @Bean
    public ItemReader<Integer> chunkListItemReader() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        return new LinkedListItemReader<>(list);
    }

}
