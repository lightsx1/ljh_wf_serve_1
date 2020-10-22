package weforward.Exception;

/**
 * 标签异常，用于删除标签时，如果还有任务使用该标签，则抛出异常
 */
public class TagException extends StatusException {

    private static final long serialVersionUID = 1L;

    public TagException() {
        super();
    }

    public TagException(String message) {
        super(message);
    }
}
