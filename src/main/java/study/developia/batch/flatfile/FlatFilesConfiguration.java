package study.developia.batch.flatfile;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import study.developia.batch.stream.CustomItemStreamReader;
import study.developia.batch.stream.CustomItemStreamWriter;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class FlatFilesConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flatFileJob() {
        return jobBuilderFactory.get("flatFileJob")
                .start(flatFileStep1())
                .next(flatFileStep2()).build();
    }

    @Bean
    public Step flatFileStep1() {
        return stepBuilderFactory.get("flatFileStep1")
                .<Customer, String>chunk(5)
                .reader(itemReader())
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> items) throws Exception {
                        System.out.println("items = " + items);
                    }
                })
                .build();
    }


    @Bean
    public ItemReader<Customer> itemReader() {
        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new ClassPathResource("/customer.csv"));

        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenize(new DelimitedLineTokenizer());
        lineMapper.setFieldSetMapper(new CustomerFieldSetMapper());

        itemReader.setLineMapper(lineMapper);
        // 첫번째 헤더이므로 라인은 건너뒴
        itemReader.setLinesToSkip(1);
        return itemReader;
    }

    @Bean
    public Step flatFileStep2() {
        return stepBuilderFactory.get("flatFileStep2")
                .tasklet(((contribution, chunkContext) -> RepeatStatus.FINISHED))
                .build();
    }


}
