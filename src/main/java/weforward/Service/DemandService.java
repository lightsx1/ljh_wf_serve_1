package weforward.Service;

import cn.weforward.common.ResultPage;
import weforward.Bo.Demand;
import weforward.Bo.Tag;
import weforward.BoImpl.DemandImpl;
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

    int STATUS_NORMAL = 1;

    int STATUS_DELETE = 2;

    /**
     * @param user
     * @param title
     * @param description
     * @param priority
     * @param charger
     * @param start
     * @param end
    * @return
      */
    Demand createDemand(String user, String title, String description, int priority, List<String> charger,
                        Date start, Date end);



    Demand createDemand(String user, String fid,String title, String description, int priority, List<String> charger, Date start, Date end);

    /**
     *
     * @param id
     * @return
     */
    Demand getDemand(String id);



    Demand deleteDemand(String id);

    /**
     *
     * @param keywords
     * @return
     */
    ResultPage<Demand> searchDemands(String keywords, int status);

    /**
     *
     * @param tagId
     * @return
     */

    ResultPage<Demand> searchDemandsByTag(String tagId);


    String addTagForDemand(String demandId,String tagId);


    String dropTagForDemand(String demandId) throws StatusException;


    ResultPage<DemandImpl> SearchSonDemand(String id);


    Tag createTag(String name);

    ResultPage<Tag> searchTagByKeywords(String keyword);

    String deleteTag(String id) throws StatusException;

    ResultPage<Demand> searchDemandByTagId(String id);

    String follow(String id);

}
