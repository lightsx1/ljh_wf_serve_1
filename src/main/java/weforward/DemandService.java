package weforward;

import cn.weforward.common.ResultPage;
import weforward.exception.DemandException;
import weforward.exception.TagException;
import weforward.impl.DemandImpl;

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

    Demand getDemand(String id);

    Tag getTag(String id);

    Demand deleteDemand(String id) throws  DemandException;

    ResultPage<Demand> searchDemands(String keywords, int D_STATUS) throws DemandException;

    String addTagForDemandByTagId(String demandId, String tagId) throws TagException, DemandException;

    String dropTagForDemandByTagId(String demandId) throws TagException;

    ResultPage<Demand> searchDemandByTagId(String id);

    String follow(String id,String user) throws DemandException;

    ResultPage <Demand> searchSonDemand(String id) throws DemandException;

    Bug createBug(String user, String demandId, String description, String priority , String dealer , String version) throws DemandException;

    Bug getBug(String id);

    ResultPage<Bug> getAllBugs(String keywords);

    Tag createTag(String name);

    List <Map < String,Integer> > analysis(String id) throws DemandException;

    ResultPage<Tag> searchTagByKeywords(String keyword);

    String deleteTag(String id) throws TagException;

    ResultPage<Bug> searchBugByDemandId(String id, String keyword);


}
