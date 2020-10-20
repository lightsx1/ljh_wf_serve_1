package weforward.Params;

import cn.weforward.framework.doc.DocPageParams;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;


@DocObject(description = "搜索参数")
public class DemandSearchParams extends DocPageParams {

    protected String keywords;

    protected int status;

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @DocAttribute(description = "搜索关键字")
    public String getKeywords() {
        return keywords;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @DocAttribute(description = "需求状态  0=全部 ,1=未完成 ,2=已完成",example = "0")
    public int getStatus() {
        return status;
    }

}
