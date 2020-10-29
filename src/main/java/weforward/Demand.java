package weforward;

import cn.weforward.common.NameItem;
import cn.weforward.common.NameItems;
import cn.weforward.common.ResultPage;
import cn.weforward.data.UniteId;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.framework.ApiException;
import weforward.di.DemandDi;
import weforward.exception.StatusException;
import weforward.exception.TagException;
import weforward.view.DemandAnalysisView;

import java.util.Date;
import java.util.Set;

/**
 * 任务Bo
 *
 */
public interface Demand {

    /** 状态-评估中*/
    NameItem STATUS_EVALUATING = NameItem.valueOf("评估中", 1);

    /** 状态-规划中*/
    NameItem STATUS_PLANNING = NameItem.valueOf("规划中", 2);

    /** 状态-待开发*/
    NameItem STATUS_ToBeDeveloped = NameItem.valueOf("待开发", 3);

    /** 状态-开发中*/
    NameItem STATUS_DEVELOPING = NameItem.valueOf("开发中", 4);

    /** 状态-待测试*/
    NameItem STATUS_ToBeTested = NameItem.valueOf("待测试", 5);

    /** 状态-测试中*/
    NameItem STATUS_TESTING = NameItem.valueOf("测试中", 6);

    /** 状态-测试通过*/
    NameItem STATUS_TESTED = NameItem.valueOf("测试通过", 7);

    /** 状态-已上线*/
    NameItem STATUS_ONLINE = NameItem.valueOf("已上线", 8);

    /** 状态-已拒绝*/
    NameItem STATUS_REJECTED = NameItem.valueOf("已拒绝", 9);

    /** 状态-挂起*/
    NameItem STATUS_HANGED = NameItem.valueOf("挂起", 10);

    /** 状态-删除*/
    NameItem STATUS_DELETED = NameItem.valueOf("删除", 999);

    /** 状态集合*/
    NameItems STATUS = NameItems.valueOf(STATUS_EVALUATING, STATUS_PLANNING, STATUS_ToBeDeveloped, STATUS_DEVELOPING,
            STATUS_ToBeTested, STATUS_TESTING, STATUS_TESTED, STATUS_ONLINE, STATUS_REJECTED, STATUS_HANGED, STATUS_DELETED);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    /** 优先级-最高级*/
    NameItem PRIORITY_HIGHEST = NameItem.valueOf("最高级", 1);

    /** 优先级-高级*/
    NameItem PRIORITY_HIGH = NameItem.valueOf("高级", 2);

    /** 优先级-中级*/
    NameItem PRIORITY_MID = NameItem.valueOf("中级", 3);

    /** 优先级-低级*/
    NameItem PRIORITY_LOW = NameItem.valueOf("低级", 4);

    /** 优先级集合*/
    NameItems PRIORITY = NameItems.valueOf(PRIORITY_HIGHEST, PRIORITY_HIGH, PRIORITY_MID, PRIORITY_LOW);

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    UniteId getId();

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    Set<String> getDealer();

    void setDealer(Set<String> Charger);

    Set<String> getFollower();

    String getCharger();

    void setCharger(String charger);

    void follow(String user);

    void disFollow(String follower) throws ApiException;

    Date getWillingStartTime();

    void setWillingStartTime(Date Start);

    Date getWillingEndTime();

    void setWillingEndTime(Date End);

    Date getEndTime();

    Date getCreateTime();

    String getCreator();

    NameItem getStatus();

    NameItem getPriority();

    String getFid();

    void setPriority(int priority);

    String getTagId();

    void setTagId(String tagId);

    /** 新建子任务时写入父任务日志中*/
    void writeSonLog();

    /** 将当前的状态扭转为评估中*/
    void toEvaluating() throws StatusException;

    /** 将当前的状态扭转为规划中*/
    void toPlanning() throws StatusException;

    /** 将当前的状态扭转为待开发*/
    void toBeDeveloped() throws StatusException;

    /** 将当前的状态扭转为开发中*/
    void toDevloping() throws StatusException;

    /** 将当前的状态扭转为待测试*/
    void toBeTested() throws StatusException;

    /** 将当前的状态扭转为测试中*/
    void toTesting() throws StatusException;

    /** 将当前的状态扭转为测试通过*/
    void toTested() throws StatusException;

    /** 将当前的状态扭转为已上线*/
    void toOnline() throws StatusException;

    /** 将当前的状态扭转为已拒绝*/
    void toRejected() throws StatusException;

    /** 将当前的状态扭转为挂起*/
    void toHanged() throws StatusException;

    /** 把任务删除，即把任务的状态设置为STATUS_DELETED（999）*/
    void delete();

    /** 为当前任务添加标签*/
    void addTagForDemand(String tagId);

    /** 删除当前任务的标签*/
    void dropTagForDemand(String demandId) throws TagException;

    /** 为当前任务添加缺陷*/
    Bug createBug(DemandDi di, String user, String demandId, String description, String priority, String dealer, String version);

    /** 缺陷分析，返回的list集合中：
     * 第一个map存储总缺陷数、已完成和未完成的数量；
     * 第二个map存储缺陷的各类数状态的数量；
     * 第三个map存储测试人员和对应处理数
     * 第四个map存储处理人员和对应的处理数
     * */
    DemandAnalysisView analysis(ResultPage<? extends Bug> rp);

    /** 获得日志*/
    ResultPage<BusinessLog> getLogs();


}
