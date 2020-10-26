package weforward.exception;

/**
 * 任务异常
 */
public class DemandException extends Exception{

    private static final long serialVersionUID = 1L;

    public DemandException() {
        super();
    }

    public DemandException(String message) {
        super(message);
    }

    public DemandException(String message, Throwable e) {
        super(message, e);
    }
}
