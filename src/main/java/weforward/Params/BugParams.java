package weforward.Params;

import cn.weforward.data.UniteId;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;
import weforward.Di.DemandDi;

import javax.annotation.Resource;
import java.util.Date;

@DocObject(description = "Bug信息")
public class BugParams {

    protected String demandId;

    protected String description;

    protected int priority;

    protected String dealer;

    protected String version;

    @DocAttribute(necessary = true, description = "需求Id", example = "")
    public String getDemandId() {
        return demandId;
    }

    public void setDemandId(String demandId) {
        this.demandId = demandId;
    }

    @DocAttribute(necessary = true, description = "Bug详情", example = "")
    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    @DocAttribute(necessary = true, description = "Bug严重性", example = "")
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @DocAttribute(necessary = true, description = "Bug处理人", example = "")
    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    @DocAttribute(necessary = true, description = "Bug处理人", example = "")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
