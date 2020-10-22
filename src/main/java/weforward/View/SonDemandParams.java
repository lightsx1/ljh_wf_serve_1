package weforward.View;

import cn.weforward.protocol.doc.annotation.DocAttribute;

public class SonDemandParams extends DemandParams {

    protected String fid;

    @DocAttribute(necessary = true, description = "父任务id")
    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}
