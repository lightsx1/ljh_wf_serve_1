package weforward;

import cn.weforward.common.NameItem;
import cn.weforward.common.NameItems;
import cn.weforward.data.UniteId;
import weforward.Exception.StatusException;

public interface Tag {

    NameItem STATUS_NORMAL = NameItem.valueOf("正常状态", 1);

    NameItem STATUS_DELETED = NameItem.valueOf("已删除", 2);

    NameItems STATUS = NameItems.valueOf(STATUS_NORMAL, STATUS_DELETED);


    UniteId getId();

    String getName();

    void setName(String name);

    NameItem getStatus();


    void deleteTag() throws StatusException;


}
