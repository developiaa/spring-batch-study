package study.developia.batch.example.batch.chunk.processor;

import org.springframework.batch.item.ItemProcessor;
import study.developia.batch.example.batch.domain.ApiRequestVO;
import study.developia.batch.example.batch.domain.ProductVO;

public class ApiItemProcessor2 implements ItemProcessor<ProductVO, ApiRequestVO> {
    @Override
    public ApiRequestVO process(ProductVO item) throws Exception {
        return ApiRequestVO.builder()
                .id(item.getId())
                .productVO(item)
                .build();
    }
}
