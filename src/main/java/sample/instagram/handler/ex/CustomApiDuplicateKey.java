package sample.instagram.handler.ex;

public class CustomApiDuplicateKey extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CustomApiDuplicateKey(String message) {
        super(message);
    }

}
