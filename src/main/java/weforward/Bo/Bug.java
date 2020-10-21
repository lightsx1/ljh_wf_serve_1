package weforward.Bo;

import cn.weforward.common.NameItem;
import cn.weforward.common.NameItems;
import cn.weforward.data.UniteId;

import java.util.Date;

public interface Bug {

    NameItem STATUS_DAIXIUZHENG = NameItem.valueOf("待修正", 1);

    NameItem STATUS_DAIFUCE = NameItem.valueOf("待复测", 2);

    NameItem STATUS_JIANYI = NameItem.valueOf("建议不做修改", 3);

    NameItem STATUS_SHENQING = NameItem.valueOf("申请无法解决", 4);

    NameItem STATUS_YIJIEJUE = NameItem.valueOf("已解决", 5);

    NameItem STATUS_BUZUOXIUGAI = NameItem.valueOf("不做修改", 6);

    NameItem STATUS_WUFAJIEJUE = NameItem.valueOf("无法解决", 7);

    NameItem STATUS_CHONGXINDAKAI = NameItem.valueOf("重新打开", 8);

    NameItems STATUS = NameItems.valueOf(STATUS_DAIXIUZHENG,STATUS_DAIFUCE,STATUS_JIANYI,
            STATUS_SHENQING,STATUS_YIJIEJUE,STATUS_BUZUOXIUGAI,STATUS_WUFAJIEJUE,STATUS_CHONGXINDAKAI);

    UniteId getId();

    String getDemandId();

    String getDescription();

    void setDescription(String description);

    int getPriority();

    void setPriority(int priority);

    int getStatus();

    void setStatus(int status);

    boolean isDealed();

    void setDealed(boolean dealed);

    String getTester();

    void setTester(String tester);

    String getDealer();

    void setDealer(String dealer);

    String getCreator();

    void setCreator(String creator);

    Date getLastTime();

    void setLastTime(Date lastTime);

    void statusTurn(int status);
}
