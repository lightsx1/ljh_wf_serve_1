package weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

@DocObject(description = "缺陷信息")
public class BugParams {

    protected String demandId;

    protected String description;

    protected String priority;

    protected String dealer;

    protected String version;

    @DocAttribute(necessary = true, description = "任务Id", example = "")
    public String getDemandId() {
        return demandId;
    }

    public void setDemandId(String demandId) {
        this.demandId = demandId;
    }

    @DocAttribute(necessary = true, description = "缺陷详情", example = "")
    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    @DocAttribute(necessary = true, description = "缺陷严重性", example = "")
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @DocAttribute(necessary = true, description = "缺陷处理人", example = "")
    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }


    @DocAttribute(necessary = true, description = "版本与平台", example = "")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
