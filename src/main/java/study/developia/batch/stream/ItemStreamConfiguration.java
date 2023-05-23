package study.developia.batch.stream;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.developia.batch.chunk.CustomItemProcessor;
import study.developia.batch.chunk.CustomItemReader;
import study.developia.batch.chunk.CustomItemWriter;
import study.developia.batch.chunk.Customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class ItemStreamConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job streamJob() {
        return jobBuilderFactory.get("streamJob")
                .start(streamStep1())
                .next(streamStep2()).build();
    }

    @Bean
    public Step streamStep1() {
        return stepBuilderFactory.get("streamStep1")
                .<String, String>chunk(5)
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public CustomItemStreamWriter itemWriter() {
        return new CustomItemStreamWriter();
    }

    @Bean
    public CustomItemStreamReader itemReader() {
        List<String> items = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            items.add(String.valueOf(i));
        }
        return new CustomItemStreamReader(items);
    }

    @Bean
    public Step streamStep2() {
        return stepBuilderFactory.get("streamStep2")
                .tasklet(((contribution, chunkContext) -> RepeatStatus.FINISHED))
                .build();
    }


}
