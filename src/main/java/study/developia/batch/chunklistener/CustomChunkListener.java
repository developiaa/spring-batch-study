package study.developia.batch.chunklistener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterChunkError;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

@Slf4j
public class CustomChunkListener {
    @BeforeChunk
    public void beforeChunk(ChunkContext chunkContext) {
        log.info("before chunk");
    }

    @AfterChunk
    public void AfterChunk(ChunkContext chunkContext) {
        log.info("after chunk");
    }

    @AfterChunkError
    public void AfterChunkError(ChunkContext chunkContext) {
        log.info("after chunk error");
    }
}
