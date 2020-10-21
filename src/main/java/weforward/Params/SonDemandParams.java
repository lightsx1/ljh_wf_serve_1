package weforward.Params;

import cn.weforward.protocol.doc.annotation.DocAttribute;

public class SonDemandParams extends DemandParams {

    protected String fid;

    @DocAttribute(necessary = true, description = "父需求id")
    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}
