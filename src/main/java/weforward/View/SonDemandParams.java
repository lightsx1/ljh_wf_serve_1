package weforward.View;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

@DocObject(description = "子任务搜索参数")
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
