package study.developia.batch.classifieritem;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ClassifierCompositeItemConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job classifierCompositionItemJob() {
        return jobBuilderFactory.get("classifierCompositionItemJob")
                .incrementer(new RunIdIncrementer())
                .start(classifierCompositionItemStep())
                .build();
    }

    @Bean
    public Step classifierCompositionItemStep() {
        return stepBuilderFactory.get("classifierCompositionItemStep")
                .<ProcessorInfo, ProcessorInfo>chunk(10)
                .reader(new ItemReader<ProcessorInfo>() {
                    int i = 0;

                    @Override
                    public ProcessorInfo read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        i++;
                        ProcessorInfo processorInfo = ProcessorInfo.builder()
                                .id(i)
                                .build();
                        return i > 3 ? null : processorInfo;
                    }
                })
                .processor(customClassifierCompositionItemProcessor())
                .writer(items -> System.out.println(items))
                .build()
                ;
    }

    @Bean
    public ItemProcessor<? super ProcessorInfo, ProcessorInfo> customClassifierCompositionItemProcessor() {
        ClassifierCompositeItemProcessor<ProcessorInfo, ProcessorInfo> processor = new ClassifierCompositeItemProcessor<>();
        ProcessorClassifier<ProcessorInfo, ItemProcessor<?,? extends ProcessorInfo>> classifier = new ProcessorClassifier<>();
        Map<Integer, ItemProcessor<ProcessorInfo, ProcessorInfo>> processorMap = new HashMap<>();
        processorMap.put(1, new CustomItemProcessor1());
        processorMap.put(2, new CustomItemProcessor2());
        processorMap.put(3, new CustomItemProcessor3());
        classifier.setProcessorMap(processorMap);
        processor.setClassifier(classifier);

        return processor;
    }
}
