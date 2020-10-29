package weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;
import weforward.Demand;
import weforward.impl.AnalysisData;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author 1
 * @create 2020/10/28 10:56
 */
@DocObject(description = "任务缺陷分析视图")
public class DemandAnalysisView {

    protected List<AnalysisData> finishList;

    protected List<AnalysisData> statusList;

    protected List<AnalysisData> testerList;

    protected List<AnalysisData> dealerList;


    public DemandAnalysisView(List<AnalysisData> finishList, List<AnalysisData> statusList, List<AnalysisData> testerList, List<AnalysisData> dealerList) {
        this.finishList = finishList;
        this.statusList = statusList;
        this.testerList = testerList;
        this.dealerList = dealerList;
    }


    public static DemandAnalysisView valueOf(List<AnalysisData> finishList, List<AnalysisData> statusList, List<AnalysisData> testerList, List<AnalysisData> dealerList) {
        if(finishList != null && statusList != null && testerList != null && dealerList != null){
            return new DemandAnalysisView(finishList, statusList, testerList, dealerList);
        }
        return null;
    }

    @DocAttribute(description = "完成情况")
    public List getFinishAmount() {
        return finishList;
    }

    @DocAttribute(description = "状态情况")
    public List getStatusAmount() {
        return statusList;
    }

    @DocAttribute(description = "测试人员和完成数")
    public List getTesterAmount() {
        return testerList;
    }

    @DocAttribute(description = "处理人员和完成数")
    public List getDealerAmount() {
        return dealerList;
    }

}