package weforward.weforward;

import cn.weforward.protocol.StatusCode;
import cn.weforward.protocol.client.execption.MicroserviceException;
import cn.weforward.protocol.support.CommonServiceCodes;
import weforward.Exception.StatusException;
import weforward.Exception.TagException;

public class DemandServiceCode extends CommonServiceCodes {
    private final static StatusCode CODE_STATUS = new StatusCode(MicroserviceException.CUSTOM_CODE_START,
            "状态异常");
    private final static StatusCode CODE_TAG = new StatusCode(
            MicroserviceException.CUSTOM_CODE_START + 1, "该标签下还有任务");
    static {
        append(CODE_STATUS, CODE_TAG);
    }

    public static int getCode(StatusException e) {
        if (e instanceof TagException) {
            return CODE_TAG.code;
        }
        return CODE_STATUS.code;
    }
}
