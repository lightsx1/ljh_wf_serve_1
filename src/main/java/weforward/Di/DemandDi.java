package weforward.Di;

import cn.weforward.common.ResultPage;
import cn.weforward.data.UniteId;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.data.persister.BusinessDi;
import weforward.Bo.Demand;
import weforward.Bo.Tag;
import weforward.Exception.StatusException;

public interface DemandDi extends BusinessDi{

    /**
     * 获得需求标题
     * @param id
     * @return
     */
    Demand getDemand(UniteId id);

    Tag getTag(UniteId id);



    /**
     * 写日志
     * @param id
     * @param action
     * @param what
     * @param note
     */
    void writeLog(UniteId id, String action, String what, String note);

    /**
     * 获取日志
     *
     * @param id
     * @return
     */
    ResultPage<BusinessLog> getLogs(UniteId id);

    void AddTagForDemand(String demandId , String tagId);

    void DropTagForDemand(String demandId) throws StatusException;
}
