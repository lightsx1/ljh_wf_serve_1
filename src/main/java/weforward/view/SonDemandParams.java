package weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

import java.util.List;

@DocObject(description = "子任务搜索参数")
public class SonDemandParams extends DemandParams {

    protected String fid;

    protected List<String> dealer;

    @DocAttribute(necessary = true, description = "父任务id")
    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    @DocAttribute(necessary = true, description = "任务处理人")
    public List<String> getDealer() {
        return dealer;
    }

    public void setDealer(List<String> dealer) {
        this.dealer = dealer;
    }
}
