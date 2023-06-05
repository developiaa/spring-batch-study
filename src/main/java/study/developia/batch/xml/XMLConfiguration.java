package study.developia.batch.xml;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import study.developia.batch.flatfile.Customer;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
//@Configuration
public class XMLConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job xmlJob() {
        return jobBuilderFactory.get("xmlJob")
                .incrementer(new RunIdIncrementer())
                .start(xmlStep1())
                .build();
    }

    @Bean
    public Step xmlStep1() {
        return stepBuilderFactory.get("xmlStep1")
                .<Customer, Customer>chunk(3)
                .reader(customXmlItemReader())
                .writer(customXmlItemWriter())
                .build()
                ;
    }

    @Bean
    public ItemReader<? extends Customer> customXmlItemReader() {
        return new StaxEventItemReaderBuilder<Customer>()
                .name("statXml")
                .resource(new ClassPathResource("customer.xml"))
                .addFragmentRootElements("customer")
                .unmarshaller(itemUnmarshaller())
                .build();
    }

    @Bean
    public Unmarshaller itemUnmarshaller() {
        Map<String, Class<?>> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);
        aliases.put("year", String.class);
        aliases.put("name", String.class);
        aliases.put("age", Integer.class);

        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        xStreamMarshaller.setAliases(aliases);

        // com.thoughtworks.xstream.security.ForbiddenClassException 이슈 발생시 처리
        xStreamMarshaller.getXStream().allowTypes(new Class[]{Customer.class});


        return xStreamMarshaller;
    }

    @Bean
    public ItemWriter<Customer> customXmlItemWriter() {
        return items -> {
            for (Customer item : items) {
                System.out.println(item.toString());
            }
        };
    }

}
