package weforward;

import cn.weforward.common.NameItem;
import cn.weforward.common.NameItems;
import cn.weforward.data.UniteId;
import weforward.exception.TagException;

public interface Tag {

    /** 标签状态-正常*/
    NameItem STATUS_NORMAL = NameItem.valueOf("正常状态", 1);

    /** 标签状态-已被删除*/
    NameItem STATUS_DELETED = NameItem.valueOf("已删除", 2);

    /** 标签状态集合*/
    NameItems STATUS = NameItems.valueOf(STATUS_NORMAL, STATUS_DELETED);

    /** 获得id*/
    UniteId getId();

    String getName();

    void setName(String name);

    NameItem getStatus();

    /** 删除标签*/
    void deleteTag() throws TagException;

}
