package weforward.ServiceImpl;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.data.persister.PersisterFactory;
import weforward.Bo.Tag;
import weforward.BoImpl.TagImpl;
import weforward.DiImpl.DemandDiImpl;
import weforward.Exception.StatusException;
import weforward.Service.TagService;

import java.util.ArrayList;
import java.util.List;

public class TagServiceImpl extends DemandDiImpl implements TagService {

    public TagServiceImpl(PersisterFactory factory) {
        super(factory);
    }

    @Override
    public Tag createTag(String name) {
        return new TagImpl(this,name);
    }

    @Override
    public ResultPage<Tag> searchTag(String keywords) {
        ResultPage<? extends Tag> rp = tagPersister.startsWith("tag");
        List<Tag> list = new ArrayList<>();
        // 产品不多的时候直接遍历过滤，多了就要考虑使用索引查询
        for (Tag tag : ResultPageHelper.toForeach(rp)) {
            if(tag.getStatus().id == 2){
                continue;
            }
            tag.getName().contains(keywords);
            list.add(tag);
        }
        return ResultPageHelper.toResultPage(list);
    }

    @Override
    public Tag deleteTag(String id) throws StatusException {
        Tag tag = tagPersister.get(id);
        tag.deleteTag();
        return tag;
    }

    @Override
    public ResultPage<Tag> getTags() {
        ResultPage<? extends Tag> rp = tagPersister.startsWith("tag");
        List<Tag> list = new ArrayList<>();
        // 产品不多的时候直接遍历过滤，多了就要考虑使用索引查询
        for (Tag tag : ResultPageHelper.toForeach(rp)) {
            if(tag.getStatus().id == 2){
                continue;
            }
            list.add(tag);
        }
        return ResultPageHelper.toResultPage(list);
    }
}
