package weforward;

import cn.weforward.common.ResultPage;
import cn.weforward.framework.ApiException;
import weforward.exception.DemandException;
import weforward.exception.TagException;
import weforward.view.DemandAnalysisView;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DemandService {

    int OPTION_NONE = 0;

    int OPTION_NOTFINISHED = 1;

    int OPTION_FINISHED = 2;

    Demand createDemand(String user, String title, String description, int priority, String charger, Set<String> dealer,
                        Date start, Date end, String tagId) throws TagException;

    Demand createSonDemand(String user, String fid, String title, String description,int priority, String charger, Set<String> dealer,
                           Date start, Date end, String tagId) throws DemandException, TagException;

    Demand getDemand(String id) throws DemandException;

    Tag getTag(String id) throws TagException;

    Demand deleteDemand(String id) throws  DemandException;

    ResultPage<Demand> searchDemands(String keywords, String creator, String follower, String dealer, String charger,int option) throws DemandException;

    void addTagForDemandByTagId(String demandId, String tagId) throws TagException, DemandException;

    void dropTagForDemandByTagId(String demandId) throws TagException, DemandException;

    ResultPage<Demand> searchDemandByTagId(String id) throws TagException;

    void followDemand(String id, String user) throws DemandException;

    void followBug(String id, String user) throws ApiException;

    ResultPage <Demand> searchSonDemand(String id) throws DemandException;

    Bug createBug(String user, String demandId, String description, String priority , String dealer , String version) throws DemandException;

    Bug getBug(String id) throws ApiException;

    Tag createTag(String name);

    DemandAnalysisView analysis(String id) throws DemandException;

    ResultPage<Tag> searchTagByKeywords(String keyword);

    void deleteTag(String id) throws TagException;

    public ResultPage<Bug> searchBugByDemandId(String id, String keyword, String k_tester, String k_dealer, int option);

}
