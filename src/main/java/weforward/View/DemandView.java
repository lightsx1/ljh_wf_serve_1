package weforward.View;

import cn.weforward.common.NameItem;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;
import weforward.Bo.Demand;

import java.util.Date;
import java.util.List;

@DocObject(description = "需求视图")
public class DemandView {

    protected Demand demand;


    public DemandView(Demand demand) {
        this.demand  = demand;
    }


    public static DemandView valueOf(Demand demand) {
        return null == demand ? null : new DemandView(demand);
    }

    @DocAttribute(description = "产品id")
    public String getId() {
        return demand.getId().getOrdinal();
    }

    @DocAttribute(description = "需求标题")
    public String getTitle() {
        return demand.getTitle();
    }

    @DocAttribute(description = "需求详情")
    public String getDescription() {
        return demand.getDescription();
    }


    @DocAttribute(description = "需求负责人")
    public List<String> getCharger() {
        return demand.getCharger();
    }


    @DocAttribute(description = "需求跟进人")
    public String getFollower() {
        return demand.getFollower();
    }


    @DocAttribute(description = "需求预计开始时间")
    public Date getStart() {
        return demand.getStart();
    }


    @DocAttribute(description = "需求预计结束时间")
    public Date getEnd() {
        return demand.getEnd();
    }


    @DocAttribute(description = "需求优先级")
    public String getPriority(){
        return demand.getPriority().getName();
    }

    @DocAttribute(description = "需求优先级")
    public String getStatus(){
        return demand.getStatus().getName();
    }

    @DocAttribute(description = "标签id")
    public String getTag(){
        return demand.getTagId();
    }
}
