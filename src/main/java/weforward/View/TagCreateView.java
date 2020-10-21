package weforward.View;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;
import weforward.Bo.Tag;


@DocObject(description = "标签视图")
public class TagCreateView {

    protected Tag tag;

    public TagCreateView(Tag tag) {
        this.tag = tag;
    }

    public static TagCreateView valueOf(Tag tag) {
        return null == tag ? null :     new TagCreateView(tag);
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
