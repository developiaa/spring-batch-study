package study.developia.batch.skip;


import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class SkipItemWriter implements ItemWriter<String> {
    private int cnt = 0;

    @Override
    public void write(List<? extends String> items) throws Exception {

        for (String item : items) {
            if (item.equals("-12")) {
                throw new SkippableException("Write failed cnt : " + cnt);
            } else {
                System.out.println("ItemWriter : " + item);
            }
        }
    }
}
