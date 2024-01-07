package study.developia.batch.example.batch.chunk.writer;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import study.developia.batch.example.batch.domain.ApiRequestVO;
import study.developia.batch.example.batch.domain.ApiResponseVO;
import study.developia.batch.example.service.AbstractApiService;

import java.util.List;

public class ApiItemWriter2 extends FlatFileItemWriter<ApiRequestVO> {
    private final AbstractApiService apiService;

    public ApiItemWriter2(AbstractApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void write(List<? extends ApiRequestVO> items) throws Exception {
        ApiResponseVO service = apiService.service(items);
        System.out.println("service = " + service);

        items.forEach(item -> item.setApiResponseVO(service));

        super.setResource(new FileSystemResource("/Users/developia/dev/study/spring-batch-study/src/main/resources/product2.txt"));
        super.open(new ExecutionContext());
        super.setLineAggregator(new DelimitedLineAggregator<>());
        super.setAppendAllowed(true);
        super.write(items);
    }
}
