package weforward;

import cn.weforward.common.NameItem;
import cn.weforward.common.NameItems;
import cn.weforward.common.ResultPage;
import cn.weforward.data.UniteId;
import cn.weforward.data.log.BusinessLog;
import weforward.Exception.StatusException;

import java.util.Date;
import java.util.List;

/**
 * 任务Bo
 *
 * @author
 */
public interface Demand {

    /**
     * 状态-评估中
     */
    NameItem STATUS_EVALUATING = NameItem.valueOf("评估中", 1);
    /**
     * 状态-规划中
     */
    NameItem STATUS_PLANNING = NameItem.valueOf("规划中", 2);
    /**
     * 状态-待开发
     */
    NameItem STATUS_ToBeDeveloped = NameItem.valueOf("待开发", 3);
    /**
     * 状态-开发中
     */
    NameItem STATUS_DEVELOPING = NameItem.valueOf("开发中", 4);
    /**
     * 状态-待测试
     */
    NameItem STATUS_ToBeTested = NameItem.valueOf("待测试", 5);
    /**
     * 状态-测试中
     */
    NameItem STATUS_TESTING = NameItem.valueOf("测试中", 6);
    /**
     * 状态-测试通过
     */
    NameItem STATUS_TESTED = NameItem.valueOf("测试通过", 7);
    /**
     * 状态-已上线
     */
    NameItem STATUS_ONLINE = NameItem.valueOf("已上线", 8);
    /**
     * 状态-已拒绝
     */
    NameItem STATUS_REJECTED = NameItem.valueOf("已拒绝", 9);
    /**
     * 状态-挂起
     */
    NameItem STATUS_HANGED = NameItem.valueOf("挂起", 10);
    /**
     * 状态-删除
     */
    NameItem STATUS_DELETED = NameItem.valueOf("删除", 999);

    NameItems STATUS = NameItems.valueOf(STATUS_EVALUATING, STATUS_PLANNING, STATUS_ToBeDeveloped, STATUS_DEVELOPING,
            STATUS_ToBeTested, STATUS_TESTING, STATUS_TESTED, STATUS_ONLINE, STATUS_REJECTED, STATUS_HANGED);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    NameItem PRIORITY_HIGHEST = NameItem.valueOf("最高级", 1);

    NameItem PRIORITY_HIGH = NameItem.valueOf("高级", 2);

    NameItem PRIORITY_MID = NameItem.valueOf("中级", 3);

    NameItem PRIORITY_LOW = NameItem.valueOf("低级", 4);

    NameItems PRIORITY = NameItems.valueOf(PRIORITY_HIGHEST, PRIORITY_HIGH, PRIORITY_MID, PRIORITY_LOW);

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    UniteId getId();

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    List<String> getCharger();

    void setCharger(List<String> Charger);

    String getFollower();

    void follow();

    Date getStart();

    void setStart(Date Start);

    Date getEnd();

    void setEnd(Date End);

    Date getEndTime();

    void setEndTime(Date End);

    Date getCreateTime();

    String getCreator();

    NameItem getStatus();

    NameItem getPriority();

    String getFid();

    void setFid(String fid);

    void setPriority(int priority);

    String getTagId();

    void setTagId(String tagId);

    void toEvaluating() throws StatusException;

    void toPlanning() throws StatusException;

    void toBeDeveloped() throws StatusException;

    void toDevloping() throws StatusException;

    void toBeTested() throws StatusException;

    void toTesting() throws StatusException;

    void toTested() throws StatusException;

    void toOnline() throws StatusException;

    void toRejected() throws StatusException;

    void toHanged() throws StatusException;

    void delete();

    void addTagForDemand(String demandId, String tagId);

    void DropTagForDemand(String demandId) throws StatusException;

    ResultPage<BusinessLog> getLogs();

    void writeSonLog(String user);

}
