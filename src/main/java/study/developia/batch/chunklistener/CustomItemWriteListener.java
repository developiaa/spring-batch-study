package study.developia.batch.chunklistener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

@Slf4j
public class CustomItemWriteListener implements ItemWriteListener {

    @Override
    public void beforeWrite(List items) {
        log.info("beforeWrite");
    }

    @Override
    public void afterWrite(List items) {
        log.info("afterWrite");
    }

    @Override
    public void onWriteError(Exception exception, List items) {
        log.info("onWriteError");
    }
}
