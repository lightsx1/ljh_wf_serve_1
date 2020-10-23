package weforward;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import weforward.Bug;
import weforward.Demand;
import weforward.Di.DemandDi;
import weforward.Tag;
import weforward.Impl.DemandImpl;
import weforward.Exception.StatusException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DemandService {

    int OPTION_NONE = 0;

    int OPTION_NOTFINISHED = 1;

    int OPTION_FINISHED = 2;

    Demand createDemand(String user, String title, String description, int priority, Set<String> charger,
                        Date start, Date end);

    Demand createDemand(String user, String fid, String title, String description, int priority, Set<String> charger, Date start, Date end) throws StatusException;

    Demand getDemand(String id);

    Demand deleteDemand(String id) throws StatusException;

    ResultPage<Demand> searchDemands(String keywords, int D_STATUS);

    String addTagForDemandByTagId(String demandId, String tagId) throws StatusException;

    String dropTagForDemandByTagId(String demandId) throws StatusException;

    ResultPage<Demand> searchDemandByTagId(String id);

    String follow(String id,String user) throws StatusException;

    ResultPage<DemandImpl> searchSonDemand(String id);

    Bug createBug(String user, String demandId, String description, String priority , String dealer , String version) throws StatusException;

    Bug getBug(String id);

    ResultPage<Bug> getAllBugs(String keywords);

    Tag createTag(String name);

    List <Map < String,Integer> > analysis(String id) throws StatusException;

    ResultPage<Tag> searchTagByKeywords(String keyword);

    String deleteTag(String id) throws StatusException;

    ResultPage<Bug> searchBugByDemandId(String id);



}
