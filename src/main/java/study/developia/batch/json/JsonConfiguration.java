package study.developia.batch.json;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import study.developia.batch.flatfile.Customer;

@RequiredArgsConstructor
@Configuration
public class JsonConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jsonJob() {
        return jobBuilderFactory.get("jsonJob")
                .incrementer(new RunIdIncrementer())
                .start(jsonStep1())
                .build();
    }

    @Bean
    public Step jsonStep1() {
        return stepBuilderFactory.get("jsonStep1")
                .<Customer, Customer>chunk(3)
                .reader(customJsonItemReader())
                .writer(customJsonItemWriter())
                .build()
                ;
    }

    @Bean
    public ItemReader<? extends Customer> customJsonItemReader() {
        return new JsonItemReaderBuilder<Customer>()
                .name("jsonReader")
                .resource(new ClassPathResource("customer.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(Customer.class))
                .build();
    }

    @Bean
    public ItemWriter<Customer> customJsonItemWriter() {
        return items -> {
            for (Customer item : items) {
                System.out.println(item.toString());
            }
        };
    }

}
