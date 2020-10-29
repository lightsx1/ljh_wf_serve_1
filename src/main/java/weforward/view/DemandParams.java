package weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

import java.util.Date;
import java.util.List;

@DocObject(description = "任务信息参数")
public class DemandParams {

    protected String title;

    protected String description;

    protected String tagId;

    protected int priority;

    protected String charger;

    protected Date willingStartTime;

    protected Date willingEndTime;

    protected List<String> dealer;

    @DocAttribute(necessary = true, description = "任务标题", example = "标题1")
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DocAttribute(necessary = true, description = "任务详情", example = "详情1")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DocAttribute(necessary = true, description = "任务负责人", example = "负责人1")
    public String getCharger() {
        return this.charger;
    }

    public void setCharger(String dealer) {
        this.charger = dealer;
    }

    @DocAttribute(necessary = true, description = "任务处理人", example = "")
    public List<String> getDealer() {
        return dealer;
    }

    public void setDealer(List<String> dealer) {
        this.dealer = dealer;
    }

    @DocAttribute(necessary = true, description = "任务预计开始时间", example = "2019-11-29T00:30:00.666Z")
    public Date getWillingStartTime() {
        return this.willingStartTime;
    }

    public void setWillingStartTime(Date willingStartTime) {
        this.willingStartTime = willingStartTime;
    }

    @DocAttribute(necessary = true, description = "任务预计结束时间", example = "2019-12-29T00:30:00.666Z")
    public Date getWillingEndTime() {
        return this.willingEndTime;
    }

    public void setWillingEndTime(Date willingEndTime) {
        this.willingEndTime = willingEndTime;
    }

    @DocAttribute(necessary = true, description = "任务优先级, 1=最高级，2=高级，3=中级，4=低级", example = "1")
    public int getPriority(){
        return this.priority;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    @DocAttribute(description = "任务所属标签")
    public String getTagId() {
        return this.tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

}
