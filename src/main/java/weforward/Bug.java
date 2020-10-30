package weforward;

import cn.weforward.common.NameItem;
import cn.weforward.common.NameItems;
import cn.weforward.common.ResultPage;
import cn.weforward.data.UniteId;
import cn.weforward.data.log.BusinessLog;
import weforward.exception.StatusException;

import java.util.Date;

public interface Bug {

    /** 缺陷状态-待修正*/
    NameItem STATUS_ToBeCorrected = NameItem.valueOf("待修正", 1);

    /** 缺陷状态-待复测*/
    NameItem STATUS_ToBeRested = NameItem.valueOf("待复测", 2);

    /** 缺陷状态-建议不做修改*/
    NameItem STATUS_SUGGEST = NameItem.valueOf("建议不做修改", 3);

    /** 缺陷状态-申请无法解决*/
    NameItem STATUS_APPLY = NameItem.valueOf("申请无法解决", 4);

    /** 缺陷状态-已解决*/
    NameItem STATUS_DONE = NameItem.valueOf("已解决", 5);

    /** 缺陷状态-不做修改*/
    NameItem STATUS_NONE = NameItem.valueOf("不做修改", 6);

    /** 缺陷状态-无法解决*/
    NameItem STATUS_CANT = NameItem.valueOf("无法解决", 7);

    /** 缺陷状态-重新打开*/
    NameItem STATUS_REOPENED = NameItem.valueOf("重新打开", 8);

    /** 缺陷状态-删除*/
    NameItem STATUS_DELETED = NameItem.valueOf("删除", 999);

    /** 缺陷状态集合*/
    NameItems STATUS = NameItems.valueOf(STATUS_ToBeCorrected, STATUS_ToBeRested, STATUS_SUGGEST,
            STATUS_APPLY, STATUS_DONE, STATUS_NONE, STATUS_CANT, STATUS_REOPENED, STATUS_DELETED);

    UniteId getId();

    String getDemandId();

    String getDescription();

    void setDescription(String description);

    String getPriority();

    void setPriority(String priority);

    NameItem getStatus();

    String getTester();

    void setTester(String tester);

    String getDealer();

    void setDealer(String dealer);

    String getCreator();

    Date getLastTime();

    void follow(String follower);

    /*将当前的状态扭转为待修正*/
    void toBeCorrected() throws StatusException;

    /*将当前的状态扭转为待复测*/
    void toBeRested() throws StatusException;

    /*将当前的状态扭转为建议不做修改*/
    void toSuggest() throws StatusException;

    /*将当前的状态扭转为申请无法解决*/
    void toApply() throws StatusException;

    /*将当前的状态扭转为已解决*/
    void toDone() throws StatusException;

    /*将当前的状态扭转为不做修改*/
    void toNone() throws StatusException;

    /*将当前的状态扭转为无法解决*/
    void toCant() throws StatusException;

    /*将当前的状态扭转为重新打开*/
    void toReopened() throws StatusException;

    /*返回当前的解决状态，true = 已解决， false = 为解决，默认为false*/
    boolean isDealed();

    /*获取缺陷日志*/
    ResultPage<BusinessLog> getLogs();
}
