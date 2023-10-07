package study.developia.batch.retrylistener;

public class CustomRetryException extends Exception {
    public CustomRetryException() {
    }

    public CustomRetryException(String message) {
        super(message);
    }
}
