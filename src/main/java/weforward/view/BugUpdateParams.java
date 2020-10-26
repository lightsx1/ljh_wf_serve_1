package weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

/**
 * @Author 1
 * @create 2020/10/22 16:54
 */

@DocObject(description = "更新缺陷参数")
public class BugUpdateParams {

    protected String bugId;

    protected String tester;

    protected String description;

    protected String priority;

    protected String dealer;

    protected String version;

    @DocAttribute( necessary = true, description = "缺陷id", example = "")
    public String getBugId() {
        return bugId;
    }

    public void setBugId(String bugId) {
        this.bugId = bugId;
    }

    @DocAttribute( description = "缺陷详情", example = "")
    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    @DocAttribute( description = "缺陷严重性", example = "")
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @DocAttribute( description = "缺陷处理人", example = "")
    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    @DocAttribute( description = "缺陷测试人", example = "")
    public String getTester() {
        return tester;
    }

    public void setTester(String tester) {
        this.tester = tester;
    }

    @DocAttribute( description = "缺陷版本与平台", example = "")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
