package study.developia.batch.itemreaderadapter;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ItemReaderAdapterConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final int chunkSize = 5;

    @Bean
    public Job itemReaderAdapterJob() {
        return jobBuilderFactory.get("itemReaderAdapterJob")
                .start(itemReaderAdapterStep())
                .build();
    }

    @Bean
    public Step itemReaderAdapterStep() {
        return stepBuilderFactory.get("itemReaderAdapterStep")
                .<String, String>chunk(chunkSize)
                .reader(customItemReaderAdapter())
                .writer(customItemReaderAdapterWriter())
                .build();
    }

    @Bean
    public ItemReader<String> customItemReaderAdapter() {
        ItemReaderAdapter<String> reader = new ItemReaderAdapter<>();
        reader.setTargetObject(customService());
        reader.setTargetMethod("customRead");
        return reader;
    }

    @Bean
    public Object customService() {
        return new CustomService();
    }

    @Bean
    public ItemWriter<String> customItemReaderAdapterWriter() {
        return items -> {
                System.out.println(items);
        };
    }

}
