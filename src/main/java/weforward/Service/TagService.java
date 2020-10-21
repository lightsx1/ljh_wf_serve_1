package weforward.Service;

import cn.weforward.common.ResultPage;
import weforward.Bo.Demand;
import weforward.Bo.Tag;
import weforward.Exception.StatusException;


public interface TagService {



    Tag createTag(String name);

    ResultPage<Tag> searchTagByKeywords(String keyword);

    String deleteTag(String id) throws StatusException;

    ResultPage<Demand> searchDemandByTagId(String id);
}
