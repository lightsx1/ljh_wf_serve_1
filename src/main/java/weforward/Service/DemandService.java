package weforward.Service;

import cn.weforward.common.ResultPage;
import weforward.Bo.Demand;
import weforward.Exception.StatusException;

import java.util.Date;
import java.util.List;

public interface DemandService {

    /** 状态-评估中 */
    int STATUS_PINGGU = 1;
    /** 状态-规划中 */
    int STATUS_GUIHUA = 2;
    /** 状态-待开发 */
    int STATUS_DAIKAIFA = 3;
    /** 状态-开发中 */
    int STATUS_KAIFAZHONG = 4;
    /** 状态-待测试 */
    int STATUS_DAICESHI = 5;
    /** 状态-测试中 */
    int STATUS_CESHIZHONG = 6;
    /** 状态-测试通过 */
    int STATUS_CESHITONGGUO = 7;
    /** 状态-已上线 */
    int STATUS_YISHANGXIAN = 8;
    /** 状态-已拒绝 */
    int STATUS_YIJUJUE = 9;
    /** 状态-挂起 */
    int STATUS_GUAQI = 10;
    /** 状态-删除 */
    int STATUS_SHANCHU = 999;



    /**
     * @param user
     * @param title
     * @param description
     * @param priority
     * @param charger
     * @param follower
     * @param start
     * @param end
    * @return
      */
    Demand createDemand(String user, String title, String description, int priority, List<String> charger, String follower,
                        Date start, Date end);

    /**
     *
     * @param id
     * @return
     */
    Demand getDemand(String id);

    Demand deleteDemand(String id);

    /**
     *
     * @param merchant
     * @param keywords
     * @return
     */
    ResultPage<Demand> searchDemands(String merchant, String keywords, int status);

    /**
     *
     * @param tagId
     * @return
     */

    ResultPage<Demand> searchDemandsByTag(String tagId);


    String addTagForDemand(String demandId,String tagId);

    String dropTagForDemand(String demandId) throws StatusException;

}
