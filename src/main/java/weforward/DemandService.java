package weforward;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import weforward.Bug;
import weforward.Demand;
import weforward.Tag;
import weforward.Impl.DemandImpl;
import weforward.Exception.StatusException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DemandService {

    int OPTION_NONE = 0;

    int OPTION_NOTFINISHED = 1;

    int OPTION_FINISHED = 2;

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
    ResultPage<Demand> searchDemands(String keywords, int D_STATUS);

    /**
     *
     * @param tagId
     * @return
     */

    ResultPage<Demand> searchDemandsByTag(String tagId);


    String addTagForDemand(String demandId,String tagId);


    String dropTagForDemand(String demandId) throws StatusException;

    ResultPage<Demand> searchDemandByTagId(String id);

    String follow(String id);

    ResultPage<DemandImpl> SearchSonDemand(String id);

    Bug createBug(String user, String demandId, String description, String priority , String dealer , String version);

    Bug getBug(String id);

    ResultPage<Bug> getAllBugs(String keywords);

    Tag createTag(String name);

    List <Map < String,Integer> > analysis(String id);

    ResultPage<Tag> searchTagByKeywords(String keyword);

    String deleteTag(String id) throws StatusException;

    ResultPage<Bug> searchBugByDemandId(String id);



}
