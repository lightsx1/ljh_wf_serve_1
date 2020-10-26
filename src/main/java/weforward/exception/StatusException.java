package weforward.exception;


/**
 * 状态异常，用于任务状态扭转时，如果扭转后状态不符合状态扭转流程设计，抛出异常
 */
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
