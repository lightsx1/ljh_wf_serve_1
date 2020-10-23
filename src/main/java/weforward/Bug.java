package weforward;

import cn.weforward.common.NameItem;
import cn.weforward.common.NameItems;
import cn.weforward.common.ResultPage;
import cn.weforward.data.UniteId;
import cn.weforward.data.log.BusinessLog;
import weforward.Exception.StatusException;

import java.util.Date;

public interface Bug {

    NameItem STATUS_ToBeCorrected = NameItem.valueOf("待修正", 1);

    NameItem STATUS_ToBeRested = NameItem.valueOf("待复测", 2);

    NameItem STATUS_SUGGEST = NameItem.valueOf("建议不做修改", 3);

    NameItem STATUS_APPLY = NameItem.valueOf("申请无法解决", 4);

    NameItem STATUS_DONE = NameItem.valueOf("已解决", 5);

    NameItem STATUS_NONE = NameItem.valueOf("不做修改", 6);

    NameItem STATUS_CANT = NameItem.valueOf("无法解决", 7);

    NameItem STATUS_REOPENED = NameItem.valueOf("重新打开", 8);

    NameItem STATUS_DELETED = NameItem.valueOf("删除", 999);

    NameItems STATUS = NameItems.valueOf(STATUS_ToBeCorrected, STATUS_ToBeRested, STATUS_SUGGEST,
            STATUS_APPLY, STATUS_DONE, STATUS_NONE, STATUS_CANT, STATUS_REOPENED, STATUS_DELETED);

    UniteId getId();

    String getDemandId();

    String getDescription();

    void setDescription(String description);

    String getPriority();

    void setPriority(String priority);

    NameItem getStatus();

    void setStatus(int status);

    String getTester();

    void setTester(String tester);

    String getDealer();

    void setDealer(String dealer);

    String getCreator();

    Date getLastTime();

    void toBeCorrected() throws StatusException;

    void toBeRested() throws StatusException;

    void toSuggest() throws StatusException;

    void toApply() throws StatusException;

    void toDone() throws StatusException;

    void toNone() throws StatusException;

    void toCant() throws StatusException;

    void toReopened() throws StatusException;

    boolean isDealed();

    ResultPage<BusinessLog> getLogs();
}
