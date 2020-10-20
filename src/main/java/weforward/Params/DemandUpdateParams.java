package weforward.Params;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

@DocObject(description = "更新需求参数")
public class DemandUpdateParams extends DemandParams{

    protected String m_Id;

    public void setId(String id) {
        m_Id = id;
    }

    @DocAttribute(necessary = true, description = "需求id")
    public String getId() {
        return m_Id;
    }
}
