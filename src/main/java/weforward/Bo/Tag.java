package weforward.Bo;

import cn.weforward.common.NameItem;
import cn.weforward.common.NameItems;
import cn.weforward.data.UniteId;
import weforward.Exception.StatusException;

import java.util.List;

public interface Tag {

    NameItem Status_NORMAL = NameItem.valueOf("正常状态", 1);

    NameItem Status_DELETE = NameItem.valueOf("已删除", 2);

    NameItems STATUS = NameItems.valueOf(Status_NORMAL,Status_DELETE);

    UniteId getId();

    String getName();

    void setName(String name);

    NameItem getStatus();

    List<String> getDid();

    void deleteTag() throws StatusException;

    void addDemandToTag(String DemandId);

    void deleteDemandFromTag(String DemandId);

}
