package weforward.ServiceImpl;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.data.log.BusinessLoggerFactory;
import cn.weforward.data.persister.PersisterFactory;
import cn.weforward.data.persister.ext.ConditionUtil;
import weforward.Bo.Demand;
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
    public ResultPage<Tag> searchTagByKeywords(String keywords) {
        ResultPage<? extends Tag> rp = tagPersister.startsWith("tag");
        List<Tag> list = new ArrayList<>();
        // 产品不多的时候直接遍历过滤，多了就要考虑使用索引查询
        for (Tag tag : ResultPageHelper.toForeach(rp)) {
            if(tag.getStatus().id == 2){
                continue;
            }
            if(!tag.getName().contains(keywords)){
                continue;
            }
            list.add(tag);
        }
        return ResultPageHelper.toResultPage(list);
    }

    @Override
    public String deleteTag(String id) throws StatusException {

        ResultPage<? extends Demand> rp = demandPersistent.search(
                ConditionUtil.eq(ConditionUtil.field("tag_Id"), id)
        );

        if(rp != null || rp.hasNext()){
            throw new StatusException("还有需求正在使用本标签，不能删除");
        }

        if(tagPersister.get(id).getStatus().id == 2){
            throw new StatusException("该标签已被删除，不能重复删除");
        }
        tagPersister.get(id).deleteTag();
        return "删除成功";
    }

    @Override
    public ResultPage<Demand> searchDemandByTagId(String id) {
        ResultPage<? extends Demand> rp = demandPersistent.search(
          ConditionUtil.eq(ConditionUtil.field("tagId"),id)
        );
        List<Demand> list = new ArrayList<>();
        // 产品不多的时候直接遍历过滤，多了就要考虑使用索引查询
        for (Demand demand : ResultPageHelper.toForeach(rp)) {
            if(demand.getStatus().id == 999){
                continue;
            }
            list.add(demand);
        }
        return ResultPageHelper.toResultPage(list);
    }


}
