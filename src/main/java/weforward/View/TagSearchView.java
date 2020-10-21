package weforward.View;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;
import weforward.Bo.Tag;

import java.util.List;

@DocObject(description = "标签视图")
public class TagSearchView {

    protected Tag tag;

    public TagSearchView(Tag tag) {
        this.tag = tag;
    }

    public static TagSearchView valueOf(Tag tag) {
        return null == tag ? null : new TagSearchView(tag);
    }

    @DocAttribute(description = "标签id")
    public String getId() {
        return tag.getId().getOrdinal();
    }

    @DocAttribute(description = "标签名称")
    public String getName() {
        return tag.getName();
    }


}
