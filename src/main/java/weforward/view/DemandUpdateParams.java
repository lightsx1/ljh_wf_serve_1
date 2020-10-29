package weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

import java.util.Date;
import java.util.List;

@DocObject(description = "更新任务参数")
public class DemandUpdateParams {

    protected String demandId;

    protected String title;

    protected String description;

    protected int priority;

    protected List<String> dealer;

    protected String charger;

    protected Date willingStartTime;

    protected Date williingEndTime;


    @DocAttribute(  description = "任务标题", example = "标题1")
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DocAttribute(  description = "任务详情", example = "详情1")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DocAttribute(  description = "任务负责人", example = "负责人1")
    public String getCharger() {
        return charger;
    }

    public void setCharger(String charger) {
        this.charger = charger;
    }

    @DocAttribute(  description = "任务处理人", example = "负责人1")
    public List<String> getDealer() {
        return this.dealer;
    }

    public void setDealer(List<String> dealer) {
        this.dealer = dealer;
    }


    @DocAttribute(  description = "任务预计开始时间", example = "2019-11-29T00:30:00.666Z")
    public Date getWillingStartTime() {
        return this.willingStartTime;
    }

    public void setWillingStartTime(Date willingStartTime) {
        this.willingStartTime = willingStartTime;
    }

    @DocAttribute(  description = "任务预计结束时间", example = "2019-12-29T00:30:00.666Z")
    public Date getWillingEndTime() {
        return this.williingEndTime;
    }

    public void setWillingEndTime(Date End) {
        this.williingEndTime = End;
    }

    @DocAttribute(  description = "任务优先级", example = "1")
    public int getPriority(){
        return this.priority;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public void setId(String id) {
        this.demandId = id;
    }

    @DocAttribute(necessary = true, description = "任务id")
    public String getId() {
        return demandId;
    }
}
