package weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;
import weforward.Demand;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DocObject(description = "子任务视图")
public class SonDemandView {

    protected Demand demand;


    public SonDemandView(Demand demand) {
        this.demand  = demand;
    }


    public static SonDemandView valueOf(Demand demand) {
        return null == demand ? null : new SonDemandView(demand);
    }

    @DocAttribute(description = "产品id")
    public String getId() {
        return demand.getId().getId();
    }

    @DocAttribute(description = "父任务id")
    public String getFid() {
        return demand.getFid();
    }

    @DocAttribute(description = "任务标题")
    public String getTitle() {
        return demand.getTitle();
    }


    @DocAttribute(description = "任务负责人")
    public String getCharger() {
        return demand.getCharger();
    }

//    @DocAttribute(description = "任务跟进人")
//    public List<String> getFollower() {
//        return new ArrayList<>(demand.getFollower());
//    }

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

    @DocAttribute(description = "任务优先级")
    public String getStatus(){
        return demand.getStatus().getName();
    }

    @DocAttribute(description = "所属标签id")
    public String getTag(){
        return demand.getTagId();
    }
}