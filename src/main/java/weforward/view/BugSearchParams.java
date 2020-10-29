package weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

/**
 * @Author 1
 * @create 2020/10/28 8:35
 */
@DocObject(description = "缺陷搜索信息")
public class BugSearchParams {

    protected String demandId;

    protected String keyword;

    protected String tester;

    protected String dealer;

    protected int option;

    @DocAttribute(necessary = true, description = "任务Id", example = "")
    public String getDemandId() {
        return demandId;
    }

    public void setDemandId(String demandId) {
        this.demandId = demandId;
    }

    @DocAttribute(description = "搜索内容关键字", example = "")
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @DocAttribute(description = "缺陷测试人", example = "")
    public String getTester() {
        return tester;
    }

    public void setTester(String tester) {
        this.tester = tester;
    }

    @DocAttribute(description = "缺陷处理人", example = "")
    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }


    @DocAttribute(necessary = true, description = "缺陷状态 0=全部，1=未完成，2=已完成，默认为0", example = "")
    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

}
