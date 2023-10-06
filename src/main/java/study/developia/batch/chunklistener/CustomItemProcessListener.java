package study.developia.batch.chunklistener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;

@Slf4j
public class CustomItemProcessListener implements ItemProcessListener<Integer, String> {

    @Override
    public void beforeProcess(Integer item) {
        log.info("beforeProcess");
    }

    @Override
    public void afterProcess(Integer item, String result) {
        log.info("afterProcess");
    }

    @Override
    public void onProcessError(Integer item, Exception e) {
        log.info("onProcessError");
    }
}
