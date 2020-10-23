package weforward.View;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;
import weforward.Demand;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
        return demand.getId().getOrdinal();
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

    @DocAttribute(description = "任务负责人")
    public Set<String> getCharger() {
        return demand.getCharger();
    }


    @DocAttribute(description = "任务跟进人")
    public String getFollower() {
        return demand.getFollower();
    }


    @DocAttribute(description = "任务预计开始时间")
    public Date getStart() {
        return demand.getStart();
    }


    @DocAttribute(description = "任务预计结束时间")
    public Date getEnd() {
        return demand.getEnd();
    }


    @DocAttribute(description = "任务优先级")
    public String getPriority(){
        return demand.getPriority().getName();
    }

    @DocAttribute(description = "任务优先级")
    public String getStatus(){
        return demand.getStatus().getName();
    }

    @DocAttribute(description = "任务结束时间")
    public Date getEndTime(){
        return demand.getEndTime();
    }

    @DocAttribute(description = "任务结束时间")
    public Date getCreateTime(){
        return demand.getCreateTime();
    }

    @DocAttribute(description = "标签id")
    public String getTag(){
        return demand.getTagId();
    }


}
