package study.developia.batch.skiplistener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;

@Slf4j
public class CustomSkipListener implements SkipListener<Integer, String> {
    @Override
    public void onSkipInRead(Throwable t) {
        log.info("onSkipInRead {}", t.getMessage());
    }

    @Override
    public void onSkipInWrite(String item, Throwable t) {
        log.info("onSkipInWrite {} {}", item, t.getMessage());
    }

    @Override
    public void onSkipInProcess(Integer item, Throwable t) {
        log.info("onSkipInProcess {} {}", item, t.getMessage());
    }
}
