package study.developia.batch.example.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import study.developia.batch.example.batch.domain.ApiInfo;
import study.developia.batch.example.batch.domain.ApiResponseVO;

@Service
public class ApiService1 extends AbstractApiService {
    @Override
    protected ApiResponseVO doApiService(RestTemplate restTemplate, ApiInfo apiInfo) {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8081/api/product/1", apiInfo, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();

        return ApiResponseVO.builder()
                .status(statusCodeValue)
                .msg(responseEntity.getBody())
                .build();
    }
}
