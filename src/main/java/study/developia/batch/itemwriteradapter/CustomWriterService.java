package study.developia.batch.itemwriteradapter;

public class CustomWriterService<T> {
    public void customWrite(T item) {
        System.out.println("item = " + item);
    }
}
