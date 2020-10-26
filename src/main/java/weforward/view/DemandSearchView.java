package weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;
import weforward.Demand;

import java.util.Date;
import java.util.Set;

@DocObject(description = "任务视图")
public class DemandSearchView {

    protected Demand demand;


    public DemandSearchView(Demand demand) {
        this.demand = demand;
    }


    public static DemandSearchView valueOf(Demand demand) {
        return null == demand ? null : new DemandSearchView(demand);
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

    @DocAttribute(description = "任务负责人")
    public Set<String> getCharger() {
        return demand.getDealer();
    }

    @DocAttribute(description = "任务优先级")
    public String getPriority() {
        return demand.getPriority().getName();
    }

    @DocAttribute(description = "任务状态")
    public String getStatus() {
        return demand.getStatus().getName();
    }

    @DocAttribute(description = "任务结束时间")
    public Date getEndTime() {
        return demand.getWillingEndTime();
    }


}

