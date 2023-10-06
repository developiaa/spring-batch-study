package study.developia.batch.chunklistener;

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
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ChunkListenerConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job chunkListenerJob() {
        return jobBuilderFactory.get("chunkListenerJob")
                .incrementer(new RunIdIncrementer())
                .start(chunkListenerStep())
                .build();
    }


    @Bean
    public Step chunkListenerStep() {
        return stepBuilderFactory.get("chunkListenerStep")
                .<Integer, String>chunk(10)
                .listener(new CustomChunkListener())
                .listener(new CustomItemReadListener())
                .listener(new CustomItemProcessListener())
                .listener(new CustomItemWriteListener())
                .reader(chunkListItemReader())
                .processor((ItemProcessor) item -> {
                    return "item" + item;
                })
                .writer((ItemWriter<String>) items -> log.info(items.toString()))
                .build();
    }

    @Bean
    public ItemReader<Integer> chunkListItemReader() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        return new ListItemReader<>(list);
    }

}
