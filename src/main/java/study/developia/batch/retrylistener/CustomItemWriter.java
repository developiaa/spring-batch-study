package study.developia.batch.retrylistener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class CustomItemWriter implements ItemWriter<String> {
    int count = 0;

    @Override
    public void write(List<? extends String> items) throws Exception {
        for (String item : items) {
            if (count < 2) {
                if (count % 2 == 0) {
                    count++;
                } else if (count % 2 == 1) {
                    count++;
                    throw new CustomRetryException("failed");
                }
            }
            log.info("write : {}", item);
        }
    }
}
