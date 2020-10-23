package weforward.View;

import cn.weforward.common.NameItem;
import cn.weforward.data.UniteId;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

@DocObject(description = "任务信息")
public class DemandParams {

    protected String title;

    protected String description;

    protected int priority;

    protected List<String> charger;

    protected Date start;

    protected Date end;

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
    public List<String> getCharger() {
        return this.charger;
    }

    public void setCharger(List<String> charger) {
        this.charger = charger;
    }


    @DocAttribute(necessary = true, description = "任务预计开始时间", example = "2019-11-29T00:30:00.666Z")
    public Date getStart() {
        return this.start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @DocAttribute(necessary = true, description = "任务预计结束时间", example = "2019-10-29T00:30:00.666Z")
    public Date getEnd() {
        return this.end;
    }

    public void setEnd(Date End) {
        this.end = End;
    }

    @DocAttribute(necessary = true, description = "任务优先级， 1=最高级，2=高级，3=中级，4=低级", example = "1")
    public int getPriority(){
        return this.priority;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }
}
