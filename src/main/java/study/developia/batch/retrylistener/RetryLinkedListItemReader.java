package study.developia.batch.retrylistener;

import io.micrometer.core.lang.Nullable;
import org.springframework.aop.support.AopUtils;
import org.springframework.batch.item.ItemReader;
import study.developia.batch.skiplistener.CustomSkipException;

import java.util.LinkedList;
import java.util.List;

public class RetryLinkedListItemReader<T> implements ItemReader<T> {
    private List<T> list;

    public RetryLinkedListItemReader(List<T> list) {
        if (AopUtils.isAopProxy(list)) {
            this.list = list;
        } else {
            this.list = new LinkedList<>(list);
        }
    }

    @Nullable
    @Override
    public T read() {
        if (!list.isEmpty()) {
            return (T) list.remove(0);
        }
        return null;
    }
}
