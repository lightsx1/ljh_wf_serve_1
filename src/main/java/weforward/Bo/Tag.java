package weforward.Bo;

import cn.weforward.common.NameItem;
import cn.weforward.common.NameItems;
import cn.weforward.data.UniteId;
import weforward.Exception.StatusException;

import java.util.List;

public interface Tag {

    NameItem Status_NORMAL = NameItem.valueOf("正常状态", 1);

    NameItem Status_DELETE = NameItem.valueOf("已删除", 2);

    NameItems STATUS = NameItems.valueOf(Status_NORMAL, Status_DELETE);

    NameItem PRIORITY_ERROR = NameItem.valueOf("功能错误", 1);

    NameItem PRIORITY_EFFECT = NameItem.valueOf("影响流程", 2);

    NameItem PRIORITY_NEWDEMAND = NameItem.valueOf("新需求", 3);

    NameItem PRIORITY_SUGGEST = NameItem.valueOf("优化建议", 4);

    NameItems PRIORITY = NameItems.valueOf(PRIORITY_ERROR, PRIORITY_EFFECT,PRIORITY_NEWDEMAND,PRIORITY_SUGGEST);

    UniteId getId();

    String getName();

    void setName(String name);

    NameItem getStatus();


    void deleteTag() throws StatusException;


}
