package weforward.view;

import cn.weforward.framework.doc.DocPageParams;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

import java.util.List;


@DocObject(description = "搜索参数")
public class DemandSearchParams extends DocPageParams {

    protected String keywords;

    protected String dealer;

    protected String charger;

    protected String creator;

    protected String follower;

    protected int option;

    @DocAttribute(description = "搜索标题关键字")
    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @DocAttribute(description = "任务状态 1=未完成 ,2=已完成, 0为全部",example = "0")
    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    @DocAttribute( description = "任务负责人", example = "")
    public String getCharger() {
        return this.charger;
    }

    public void setCharger(String dealer) {
        this.charger = dealer;
    }


    @DocAttribute( description = "任务处理人", example = "")
    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    @DocAttribute( description = "任务跟进人", example = "")
    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }


    @DocAttribute(necessary = true, description = "任务创建人", example = "")
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

}
