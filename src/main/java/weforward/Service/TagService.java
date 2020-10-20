package weforward.Service;

import cn.weforward.common.ResultPage;
import weforward.Bo.Tag;
import weforward.Exception.StatusException;


public interface TagService {

    Tag createTag(String name);

    ResultPage<Tag> searchTag(String keyword);

    Tag deleteTag(String id) throws StatusException;

    ResultPage<Tag> getTags();
}
