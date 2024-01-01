package study.developia.batch.example.batch.chunk.writer;

import org.springframework.batch.item.ItemWriter;
import study.developia.batch.example.batch.domain.ApiRequestVO;
import study.developia.batch.example.batch.domain.ApiResponseVO;
import study.developia.batch.example.service.AbstractApiService;

import java.util.List;

public class ApiItemWriter2 implements ItemWriter<ApiRequestVO> {
    private final AbstractApiService apiService;

    public ApiItemWriter2(AbstractApiService apiService) {
        this.apiService = apiService;
    }
    @Override
    public void write(List<? extends ApiRequestVO> items) throws Exception {
        ApiResponseVO service = apiService.service(items);
        System.out.println("service = " + service);
    }
}
