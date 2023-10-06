package study.developia.batch.chunklistener;


import lombok.extern.slf4j.Slf4j;

import javax.batch.api.chunk.listener.ItemReadListener;

@Slf4j
public class CustomItemReadListener implements ItemReadListener {

    @Override
    public void beforeRead() throws Exception {
        log.info("beforeRead");
    }

    @Override
    public void afterRead(Object item) throws Exception {
        log.info("afterRead");

    }

    @Override
    public void onReadError(Exception ex) throws Exception {
        log.info("onReadError");

    }
}
