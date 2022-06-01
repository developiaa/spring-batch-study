package study.developia.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 스프링 배치가 작동하기 위해 선언
 * 총 4개의 설정 클래스 실행, 스프링 배치의 모든 초기화 및 실행 구성
 * 빈으로 등록된 모든 job을 검색해서 초기화와 동시에 job을 수행하도록 구성
 */

@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchStudyApplication.class, args);
    }

}
