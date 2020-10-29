package weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;
import weforward.Bug;

import java.util.Date;

@DocObject(description = "缺陷视图")
public class BugView {

    protected Bug bug;

    public BugView(Bug bug) {
        this.bug = bug;
    }

    public static BugView valueOf(Bug bug) {
        return null == bug ? null : new BugView(bug);
    }

    @DocAttribute(description = "缺陷id")
    public String getId() {
        return bug.getId().getId();
    }

    @DocAttribute(description = "缺陷详情")
    public String getDescription() {
        return bug.getDescription();
    }

    @DocAttribute(description = "缺陷状态")
    public String getStatus() {
        return bug.getStatus().getName();
    }

    @DocAttribute(description = "缺陷严重性")
    public String getPriority() {
        return bug.getPriority();
    }

    @DocAttribute(description = "测试人")
    public String getTester() {
        return bug.getTester();
    }

    @DocAttribute(description = "处理人")
    public String getDealer() {
        return bug.getDealer();
    }

    @DocAttribute(description = "创建人")
    public String getCreator() {
        return bug.getCreator();
    }

    @DocAttribute(description = "最后处理日期")
    public Date getLastTime() {
        return bug.getLastTime();
    }

}
