package weforward.weforward;

import cn.weforward.protocol.StatusCode;
import cn.weforward.protocol.client.execption.MicroserviceException;
import cn.weforward.protocol.support.CommonServiceCodes;
import weforward.exception.DemandException;
import weforward.exception.TagException;

public class DemandServiceCode extends CommonServiceCodes {
    private final static StatusCode CODE_STATUS = new StatusCode(MicroserviceException.CUSTOM_CODE_START,
            "任务状态异常");

    private final static StatusCode CODE_TAG = new StatusCode(
            MicroserviceException.CUSTOM_CODE_START + 1, "标签异常");

    private final static StatusCode CODE_DEMAND = new StatusCode(
            MicroserviceException.CUSTOM_CODE_START + 2, "需求异常");

    static {
        append(CODE_STATUS, CODE_TAG,CODE_DEMAND);
    }

    public static int getCode(Exception e) {
        if (e instanceof TagException) {
            return CODE_TAG.code;
        }
        if(e instanceof DemandException){
            return CODE_DEMAND.code;
        }
        return CODE_STATUS.code;
    }
}
