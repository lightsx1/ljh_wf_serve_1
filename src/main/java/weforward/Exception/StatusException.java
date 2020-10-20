package weforward.Exception;

public class StatusException extends Exception {

    private static final long serialVersionUID = 1L;

    public StatusException() {
        super();
    }

    public StatusException(String message) {
        super(message);
    }

    public StatusException(String message, Throwable e) {
        super(message, e);
    }

}
