package weforward.ServiceImpl;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.data.log.BusinessLoggerFactory;
import cn.weforward.data.persister.PersisterFactory;
import cn.weforward.data.persister.ext.ConditionUtil;
import cn.weforward.framework.support.Global;
import weforward.Bo.Demand;
import weforward.Bo.Tag;
import weforward.BoImpl.DemandImpl;
import weforward.BoImpl.TagImpl;
import weforward.DiImpl.DemandDiImpl;
import weforward.Exception.StatusException;
import weforward.Service.DemandService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DemandServiceImpl extends DemandDiImpl implements DemandService {

    public DemandServiceImpl(PersisterFactory factory, BusinessLoggerFactory loggerFactory) {
        super(factory, loggerFactory);
    }

    private String getUser() {
        String user = Global.TLS.getValue("user");
        if (null == user) {
            user = "admin";
        }
        return user;
    }

    @Override
    public Demand createDemand(String user, String title, String description, int priority, List<String> charger,  Date start, Date end) {
        return new DemandImpl(this, user, title, description, priority, charger, start, end);
    }

    @Override
    public Demand createDemand(String user, String fid,String title, String description, int priority, List<String> charger, Date start, Date end) {
        return new DemandImpl(this, user, fid,title, description, priority, charger, start, end);
    }

    @Override
    public Demand getDemand(String id) {
        return demandPersistent.get(id);
    }

    @Override
    public Demand deleteDemand(String id) {
        Demand demand = demandPersistent.get(id);
        demand.delete();
        return demand;
    }

    @Override
    public ResultPage<Demand> searchDemands(String keywords, int status) {

        ResultPage<? extends Demand> rp = demandPersistent.search(
                ConditionUtil.eq(ConditionUtil.field("fid"), (String) null)
        );

        List<Demand> list = new ArrayList<>();
        for (Demand demand : ResultPageHelper.toForeach(rp)) {
            if (!isMatch(demand, keywords)) {
                continue;
            }
            if (!isMatch(demand, status)) {
                continue;
            }
            if(demand.getStatus().id == STATUS_SHANCHU){
                continue;
            }
            list.add(demand);
        }
        return ResultPageHelper.toResultPage(list);
    }

    @Override
    public ResultPage<Demand> searchDemandsByTag(String tagId) {
        return null;
    }

    @Override
    public String addTagForDemand(String demandId, String tagId) {
        demandPersistent.get(demandId).addTagForDemand(demandId,tagId);
        return "添加成功";
    }

    @Override
    public String dropTagForDemand(String demandId) throws StatusException {
        demandPersistent.get(demandId).DropTagForDemand(demandId);
        return "删除标签成功";
    }

    @Override
    public ResultPage <DemandImpl> SearchSonDemand(String id) {
        ResultPage<DemandImpl> rp = demandPersistent.search(
         ConditionUtil.and(
                ConditionUtil.eq(ConditionUtil.field("fid"),id)
        ));
        return rp;
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

            if(tag.getStatus().id == STATUS_DELETE){
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
                ConditionUtil.eq(ConditionUtil.field("tagId"), id)
        );

        if(rp != null && rp.hasNext()){
            throw new StatusException("还有需求正在使用本标签，不能删除");
        }

        if(tagPersister.get(id).getStatus().id == STATUS_DELETE){
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
            if(demand.getStatus().id == STATUS_SHANCHU){
                continue;
            }
            list.add(demand);
        }
        return ResultPageHelper.toResultPage(list);
    }

    @Override
    public String follow(String id) {
        demandPersistent.get(id).follow();
        return "跟进成功";
    }


    private static boolean isMatch(Demand demand, String keywords) {
        if (null == demand || demand.getStatus().id==STATUS_SHANCHU) {
            return false;
        }
        String m_title = demand.getTitle();
        String m_creator = demand.getCreator();
        String m_follower = demand.getFollower();


        if (StringUtil.isEmpty(m_title) && StringUtil.isEmpty(m_creator) && StringUtil.isEmpty(m_follower) && demand.getCharger().size()==0) {
            return false;
        }

        boolean result = false;
        for(String charger : demand.getCharger()){
            if(charger.contains(keywords)) result = true;
        }

        return m_title.contains(keywords) || m_creator.contains(keywords) || m_follower.contains(keywords) || result;
    }

    private static boolean isMatch(Demand demand, int status) {

        if (null == demand || demand.getStatus().id==STATUS_SHANCHU) {
            return false;
        }

        NameItem m_status = demand.getStatus();

        if (status == 2) {
            if (m_status.id == STATUS_PINGGU ||  m_status.id == STATUS_GUIHUA || m_status.id == STATUS_DAIKAIFA || m_status.id == STATUS_KAIFAZHONG || m_status.id == STATUS_DAICESHI || m_status.id == STATUS_CESHIZHONG || m_status.id == STATUS_CESHITONGGUO) {
                return false;
            }
        } else if (status == 1) {
            if (m_status.id == STATUS_YISHANGXIAN || m_status.id == STATUS_YIJUJUE || m_status.id == STATUS_GUAQI) {
                return false;
            }
        }
        return true;
    }

}



/*
*         if (st) {
            return false;
        }
        if (STATUS_PINGGU == status && m_status.id != Demand.STATUS_PINGGU.id) {
            return false;
        }
        if (STATUS_GUIHUA == status && m_status.id != Demand.STATUS_GUIHUA.id) {
            return false;
        }
        if (STATUS_DAIKAIFA == status && m_status.id != Demand.STATUS_DAIKAIFA.id) {
            return false;
        }
        if (STATUS_KAIFAZHONG == status && m_status.id != Demand.STATUS_KAIFAZHONG.id) {
            return false;
        }
        if (STATUS_DAICESHI == status && m_status.id != Demand.STATUS_DAICESHI.id) {
            return false;
        }
        if (STATUS_CESHIZHONG == status && m_status.id != Demand.STATUS_CESHIZHONG.id) {
            return false;
        }
        if (STATUS_CESHITONGGUO== status && m_status.id != Demand.STATUS_CESHITONGGUO.id) {
            return false;
        }
        if (STATUS_YISHANGXIAN == status && m_status.id != Demand.STATUS_YISHANGXIAN.id) {
            return false;
        }
        if (STATUS_GUAQI == status && m_status.id != Demand.STATUS_GUAQI.id) {
            return false;
        }
* */