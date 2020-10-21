package weforward.Bo;

import cn.weforward.common.NameItem;
import cn.weforward.common.NameItems;
import cn.weforward.data.UniteId;
import weforward.Exception.StatusException;

import java.util.Date;
import java.util.List;

/**
 * 需求Bo
 *
 * @author
 */
public interface Demand {

    /**
     * 状态-评估中
     */
    NameItem STATUS_PINGGU = NameItem.valueOf("评估中", 1);
    /**
     * 状态-规划中
     */
    NameItem STATUS_GUIHUA = NameItem.valueOf("规划中", 2);
    /**
     * 状态-待开发
     */
    NameItem STATUS_DAIKAIFA = NameItem.valueOf("待开发", 3);
    /**
     * 状态-开发中
     */
    NameItem STATUS_KAIFAZHONG = NameItem.valueOf("开发中", 4);
    /**
     * 状态-待测试
     */
    NameItem STATUS_DAICESHI = NameItem.valueOf("待测试", 5);
    /**
     * 状态-测试中
     */
    NameItem STATUS_CESHIZHONG = NameItem.valueOf("测试中", 6);
    /**
     * 状态-测试通过
     */
    NameItem STATUS_CESHITONGGUO = NameItem.valueOf("测试通过", 7);
    /**
     * 状态-已上线
     */
    NameItem STATUS_YISHANGXIAN = NameItem.valueOf("已上线", 8);
    /**
     * 状态-已拒绝
     */
    NameItem STATUS_YIJUJUE = NameItem.valueOf("已拒绝", 9);
    /**
     * 状态-挂起
     */
    NameItem STATUS_GUAQI = NameItem.valueOf("挂起", 10);
    /**
     * 状态-挂起
     */
    NameItem STATUS_SHANCHU = NameItem.valueOf("删除", 999);

    NameItems STATUS = NameItems.valueOf(STATUS_PINGGU, STATUS_GUIHUA, STATUS_DAIKAIFA, STATUS_KAIFAZHONG,
            STATUS_DAICESHI, STATUS_CESHIZHONG, STATUS_CESHITONGGUO, STATUS_YISHANGXIAN, STATUS_YIJUJUE, STATUS_GUAQI);

    /**
     * 优先级-最高级
     */
    NameItem PRIORITY_HIGHEST = NameItem.valueOf("最高级", 1);

    /**
     * 优先级-高级
     */
    NameItem PRIORITY_HIGH = NameItem.valueOf("高级", 2);

    /**
     * 优先级-高级
     */
    NameItem PRIORITY_MID = NameItem.valueOf("中级", 3);

    /**
     * 优先级-低级
     */
    NameItem PRIORITY_LOW = NameItem.valueOf("低级", 4);

    NameItems PRIORITY = NameItems.valueOf(PRIORITY_HIGHEST, PRIORITY_HIGH, PRIORITY_MID, PRIORITY_LOW);

    /**
     * 获得唯一id
     *
     * @return
     */
    UniteId getId();

    /**
     * 获得需求标题
     *
     * @return 需求标题
     */
    String getTitle();

    /**
     * 设置需求标题
     *
     * @param title
     */
    void setTitle(String title);

    /**
     * 获得需求具体描述
     *
     * @return 具体描述
     */
    String getDescription();

    /**
     * 设置需求具体描述
     *
     * @param description
     */
    void setDescription(String description);

    /**
     * 获得需求负责人名称
     *
     * @return 负责人名称
     */
    List<String> getCharger();

    /**
     * 设置需求负责人
     *
     * @param Charger
     */
    void setCharger(List<String> Charger);

    /**
     * 获得需求跟进人名称
     *
     * @return 需求跟进人名称
     */
    String getFollower();

    /**
     * 设置需求跟进人
     *
     */
    void follow();

    /**
     * 获得需求预期开始时间
     *
     * @return 需求预期开始时间
     */
    Date getStart();

    /**
     * 设置需求预期开始时间
     *
     * @param Start
     */
    void setStart(Date Start);

    /**
     * 获得需求预期结束时间
     *
     * @return 需求预期结束时间
     */
    Date getEnd();

    /**
     * 设置需求预期结束时间
     *
     * @param End
     */
    void setEnd(Date End);

    /**
     * 获得需求结束时间
     *
     * @return 需求结束时间
     */
    Date getEndTime();

    /**
     * 设置需求结束时间
     *
     * @param End
     */
    void setEndTime(Date End);

    /**
     * 获得需求创建时间
     *
     * @return 创建时间
     */
    Date getCreateTime();

    /**
     * 获得需求创建人名称
     *
     * @return 需求创建人名称
     */
    String getCreator();

    /**
     * 获得需求当前状态
     *
     * @return 需求当前状态
     */
    NameItem getStatus();

    /**
     * 获得需求当前优先级
     *
     * @return 需求当前优先级
     */
    NameItem getPriority();

    String getFid();

    void setFid(String fid);

    void setPriority(int priority);

    String getTagId();

    void setTagId(String tagId);

    String getUser();

    void statusTurn(int status);

    void delete();

    void addTagForDemand(String demandId, String tagId);

    void DropTagForDemand(String demandId) throws StatusException;

}
