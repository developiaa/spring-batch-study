package study.developia.batch.example.batch.classifier;


import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;
import study.developia.batch.example.batch.domain.ApiRequestVO;
import study.developia.batch.example.batch.domain.ProductVO;

import java.util.HashMap;
import java.util.Map;

public class ProcessorClassifier<C,T> implements Classifier<C,T> {
    private Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap = new HashMap<>();
    @Override
    public T classify(C classifiable) {
        return (T) processorMap.get(((ProductVO) classifiable).getType());
    }

    public void setProcessorMap(Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap) {
        this.processorMap = processorMap;
    }
}
