package weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;
import weforward.Tag;


@DocObject(description = "标签视图")
public class TagView {

    protected Tag tag;

    public TagView(Tag tag) {
        this.tag = tag;
    }

    public static TagView valueOf(Tag tag) {
        return null == tag ? null :     new TagView(tag);
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
