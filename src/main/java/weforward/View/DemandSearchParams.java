package weforward.View;

import cn.weforward.framework.doc.DocPageParams;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;


@DocObject(description = "搜索参数")
public class DemandSearchParams extends DocPageParams {

    protected String keywords;

    protected int option;

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @DocAttribute(description = "搜索关键字")
    public String getKeywords() {
        return keywords;
    }

    public void setOption(int option) {
        this.option = option;
    }

    @DocAttribute(description = "任务状态 1=未完成 ,2=已完成, 其他数字为全部",example = "0")
    public int getOption() {
        return option;
    }

}
