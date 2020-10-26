package weforward.exception;

/**
 * 标签异常，用于标签不存在，或者删除标签时仍有任务使用该标签时使用
 */
public class TagException extends Exception {

    private static final long serialVersionUID = 1L;

    public TagException() {
        super();
    }

    public TagException(String message) {
        super(message);
    }

    public TagException(String message, Throwable e) {
        super(message, e);
    }
}
