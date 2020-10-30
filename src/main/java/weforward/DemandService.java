package weforward;

import cn.weforward.common.ResultPage;
import cn.weforward.framework.ApiException;
import weforward.exception.DemandException;
import weforward.exception.TagException;
import weforward.view.DemandAnalysisView;

import java.util.Date;
import java.util.Set;

public interface DemandService {

    int OPTION_NONE = 0;

    int OPTION_NOTFINISHED = 1;

    int OPTION_FINISHED = 2;

    /** 生成父任务*/
    Demand createDemand(String user, String title, String description, int priority, String charger, Set<String> dealer,
                        Date start, Date end, String tagId) throws TagException;
    /** 生成子任务*/
    Demand createSonDemand(String user, String fid, String title, String description,int priority, String charger, Set<String> dealer,
                           Date start, Date end, String tagId) throws DemandException, TagException;

    /** 根据id获得单个任务*/
    Demand getDemand(String id) throws DemandException;

    /** 根据id获得单个标签*/
    Tag getTag(String id) throws TagException;

    /** 删除任务*/
    Demand deleteDemand(String id) throws  DemandException;

    /** 根据关键字搜索任务*/
    ResultPage<Demand> searchDemands(String keywords, String creator, String follower, String dealer, String charger,int option) throws DemandException;

    /** 为任务添加标签*/
    void addTagForDemandByTagId(String demandId, String tagId) throws TagException, DemandException;

    /** 为任务删除标签*/
    void dropTagForDemandByTagId(String demandId) throws TagException, DemandException;

    /** 搜索出标签下的所有任务*/
    ResultPage<Demand> searchDemandByTagId(String id) throws TagException;

    /** 跟进任务*/
    void followDemand(String id, String user) throws DemandException;

    /** 跟进缺陷*/
    void followBug(String id, String user) throws ApiException;

    /** 搜索子任务*/
    ResultPage <Demand> searchSonDemand(String id) throws DemandException;

    /** 创建缺陷*/
    Bug createBug(String user, String demandId, String description, String priority , String dealer , String version) throws DemandException;

    /** 根据id获取单个缺陷*/
    Bug getBug(String id) throws ApiException;

    /** 创建标签*/
    Tag createTag(String name);

    /** 创建分析*/
    DemandAnalysisView analysis(String id) throws DemandException;

    /** 根据关键字搜索标题*/
    ResultPage<Tag> searchTagByKeywords(String keyword);

    /** 删除标签*/
    void deleteTag(String id) throws TagException;

    /** 获得任务下的所有缺陷*/
    ResultPage<Bug> searchBugByDemandId(String id, String keyword, String k_tester, String k_dealer, int option);

}
