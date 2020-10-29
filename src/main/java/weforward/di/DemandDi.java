package weforward.di;

import cn.weforward.common.ResultPage;
import cn.weforward.data.UniteId;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.data.persister.BusinessDi;
import weforward.Bug;
import weforward.Demand;
import weforward.Tag;
import weforward.impl.AnalysisData;
import weforward.view.DemandAnalysisView;

import java.util.List;


public interface DemandDi extends BusinessDi{

    /**
     * 从持久器中获得任务
     * @param id
     * @return
     */
    Demand getDemand(UniteId id);

    
    /**
     * 从持久器中获得标签
     * @param id
     * @return 
     */
    Tag getTag(UniteId id);


    /**
     * 从持久器中获得缺陷
     * @param id
     * @return
     */
    Bug getBug(UniteId id);


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

    /*
    *  新建缺陷
    * */
    Bug createBug(DemandDi di, String user, String demandId, String description, String priority, String dealer,String version);

    /*
     *  缺陷分析
     * */
    DemandAnalysisView analysis(ResultPage<? extends Bug> rp);

}
