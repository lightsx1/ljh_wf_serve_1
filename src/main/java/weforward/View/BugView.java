package weforward.View;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;
import weforward.Bo.Bug;

import java.util.Date;

@DocObject(description = "Bug视图")
public class BugView {

    protected Bug bug;

    public BugView(Bug bug) {
        this.bug = bug;
    }

    public static BugView valueOf(Bug bug) {
        return null == bug ? null : new BugView(bug);
    }

    @DocAttribute(description = "Bug_id")
    public String getId() {
        return bug.getId().getOrdinal();
    }

    @DocAttribute(description = "Bug详情")
    public String getDescription() {
        return bug.getDescription();
    }

    @DocAttribute(description = "Bug状态")
    public int getStatus() {
        return bug.getStatus();
    }

    @DocAttribute(description = "Bug严重性")
    public int getPriority() {
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
