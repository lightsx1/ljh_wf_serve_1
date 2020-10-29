package weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;
import weforward.Demand;

import java.util.*;

@DocObject(description = "任务视图")
public class DemandView {

    protected Demand demand;


    public DemandView(Demand demand) {
        this.demand  = demand;
    }


    public static DemandView valueOf(Demand demand) {
        return null == demand ? null : new DemandView(demand);
    }

    @DocAttribute(description = "任务id")
    public String getId() {
        return demand.getId().getId();
    }

    @DocAttribute(description = "任务标题")
    public String getTitle() {
        return demand.getTitle();
    }

    @DocAttribute(description = "任务详情")
    public String getDescription() {
        return demand.getDescription();
    }

    @DocAttribute(description = "任务创建人")
    public String getCreator() {
        return demand.getCreator();
    }

    @DocAttribute(description = "任务处理人")
    public List<String> getDealer() {
        return new ArrayList<>(demand.getDealer());
    }

    @DocAttribute(description = "任务负责人")
    public String getCharger() {
        return demand.getCharger();
    }

    @DocAttribute(description = "任务跟进人")
    public List<String> getFollower() {
        return new ArrayList<>(demand.getFollower());
    }

    @DocAttribute(description = "任务预计开始时间")
    public Date getWillingStartTime() {
        return demand.getWillingStartTime();
    }

    @DocAttribute(description = "任务预计结束时间")
    public Date getWillingEndTime() {
        return demand.getWillingEndTime();
    }

    @DocAttribute(description = "任务优先级")
    public String getPriority(){
        return demand.getPriority().getName();
    }

    @DocAttribute(description = "任务状态")
    public String getStatus(){
        return demand.getStatus().getName();
    }

    @DocAttribute(description = "任务结束时间")
    public Date getEndTime(){
        return demand.getEndTime();
    }

    @DocAttribute(description = "任务创建时间")
    public Date getCreateTime(){
        return demand.getCreateTime();
    }

    @DocAttribute(description = "标签id")
    public String getTag(){
        return demand.getTagId();
    }


}
