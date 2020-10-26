package weforward.impl;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.data.log.BusinessLoggerFactory;
import cn.weforward.data.persister.PersisterFactory;
import cn.weforward.data.persister.ext.ConditionUtil;
import weforward.Bug;
import weforward.Demand;
import weforward.exception.DemandException;
import weforward.exception.TagException;
import weforward.Tag;
import weforward.DemandService;

import java.util.*;

public class DemandServiceImpl extends DemandDiImpl implements DemandService {

    public DemandServiceImpl(PersisterFactory factory, BusinessLoggerFactory loggerFactory) {
        super(factory, loggerFactory);
    }

    @Override
    public Demand createDemand(String user, String title, String description, int priority, String charger, Set<String> dealer, Date willingStartTime, Date willingEndTime, String tagId) throws TagException {

        if( tagId !=null && (tagPersister.get(tagId) == null || tagPersister.get(tagId).getStatus()==Tag.STATUS_DELETED) ){
            throw new TagException("新增任务出错！不存在该id的标签");
        }
        return new DemandImpl(this, user, title, description, priority, charger, dealer, willingStartTime, willingEndTime, tagId);
    }

    @Override
    public Demand createSonDemand(String user, String fid, String title, String description, int priority, String charger, Set<String> dealer, Date willingStartTime, Date willingEndTime, String tagId) throws DemandException, TagException {

        if (demandPersistent.get(fid) == null|| demandPersistent.get(fid).getStatus().id == Demand.STATUS_DELETED.id ) {
            throw new DemandException("新增子任务出错！不存在该id的父任务");
        }
        if( tagId !=null && (tagPersister.get(tagId) == null || tagPersister.get(tagId).getStatus()==Tag.STATUS_DELETED) ){
            throw new TagException("新增子任务出错！不存在该id的标签");
        }
        return new DemandImpl(this, user, fid, title, description, priority, charger, dealer, willingStartTime, willingEndTime);
    }

    @Override
    public Bug createBug(String user, String demandId, String description, String priority, String dealer, String version) throws DemandException {
        if (demandPersistent.get(demandId) == null || demandPersistent.get(demandId).getStatus().id == Demand.STATUS_DELETED.id) {
            throw new DemandException("新增出错！不存在该id的任务");
        }
        return demandPersistent.get(demandId).createBug(this, user, demandId, description, priority, dealer, version);
    }

    @Override
    public Tag createTag(String name) {
        return new TagImpl(this, name);
    }

    @Override
    public Bug getBug(String id) {
        return bugPersister.get(id);
    }

    @Override
    public Demand getDemand(String id) {
        return demandPersistent.get(id);
    }

    @Override
    public Tag getTag(String id) {
        return tagPersister.get(id);
    }

    @Override
    public Demand deleteDemand(String id) throws DemandException {
        Demand demand = demandPersistent.get(id);
        if (demand == null || demand.getStatus().id == Demand.STATUS_DELETED.id) {
            throw new DemandException("删除失败！不存在该id的任务");
        }
        demand.delete();
        return demand;
    }

    @Override
    public ResultPage<Demand> searchDemands(String keywords, int option) throws DemandException {

        if(option != 0 && option!= 1 && option != 2){
            throw new DemandException("请填写正确的任务状态，只能为0,1,2");
        }

        ResultPage<? extends Demand> rp = demandPersistent.search(
                ConditionUtil.and(
                        ConditionUtil.eq(ConditionUtil.field("fid"), (String) null), ConditionUtil.ne(ConditionUtil.field("status"), Demand.STATUS_DELETED.id)
                ));

        List<Demand> list = new ArrayList<>();
        for (Demand demand : ResultPageHelper.toForeach(rp)) {
            if (!isMatch(demand, option)) {
                continue;
            }
            if (!isMatch(demand, keywords)) {
                continue;
            }
            list.add(demand);
        }
        return ResultPageHelper.toResultPage(list);
    }

    @Override
    public String addTagForDemandByTagId(String demandId, String tagId) throws TagException, DemandException {
        Demand demand = demandPersistent.get(demandId);
        Tag tag = tagPersister.get(tagId);

        if (demand == null || demand.getStatus().id == Demand.STATUS_DELETED.id) {
            throw new DemandException("任务添加标签出错！不存在该id的任务");
        }


        if (tag == null || tag.getStatus().id == Tag.STATUS_DELETED.id) {
            throw new TagException("任务添加标签出错！不存在该id的标签");
        }

        demandPersistent.get(demandId).addTagForDemand(demandId, tagId);
        return "添加成功";
    }

    @Override
    public String dropTagForDemandByTagId(String demandId) throws TagException {

        Demand demand = demandPersistent.get(demandId);
        if (demand == null || demand.getStatus().id == Demand.STATUS_DELETED.id) {
            throw new TagException("任务删除标签出错！不存在该id的任务");
        }

        if(demand.getTagId()==null || demand.getTagId().equals("")){
            throw new TagException("任务的标签为空，不能删除标签");
        }

        demand.dropTagForDemand(demandId);
        return "删除标签成功";
    }

    @Override
    public ResultPage <Demand> searchSonDemand(String id) throws DemandException {

        Demand demand = demandPersistent.get(id);
        if(demand == null || demand.getStatus() == Demand.STATUS_DELETED){
            throw new DemandException("任务搜索出错！不存在该id的父任务");
        }

        ResultPage<? extends Demand> rp = demandPersistent.search(
                ConditionUtil.and(
                        ConditionUtil.eq(ConditionUtil.field("fid"), id), ConditionUtil.ne(ConditionUtil.field("status"), Demand.STATUS_DELETED.id)
                )
        );

        return (ResultPage<Demand>) rp;
    }


    @Override
    public ResultPage<Tag> searchTagByKeywords(String keywords) {
        ResultPage<? extends Tag> rp = tagPersister.startsWith("tag");
        List<Tag> list = new ArrayList<>();
        for (Tag tag : ResultPageHelper.toForeach(rp)) {

            if (tag.getStatus().id == Tag.STATUS_DELETED.id) {
                continue;
            }
            if (!tag.getName().contains(keywords)) {
                continue;
            }
            list.add(tag);
        }
        return ResultPageHelper.toResultPage(list);
    }

    @Override
    public String deleteTag(String id) throws TagException {

        Tag tag = tagPersister.get(id);
        if (tag == null || tag.getStatus().id == Tag.STATUS_DELETED.id) {
            throw new TagException("不存在该id的标签");
        }

        ResultPage<? extends Demand> rp = demandPersistent.search(
                ConditionUtil.and(
                        ConditionUtil.eq(ConditionUtil.field("tagId"), id), ConditionUtil.ne(ConditionUtil.field("status"), Demand.STATUS_DELETED.id)
                )
        );

        if (rp.getCount() != 0) {
            throw new TagException("还有需求正在使用本标签，不能删除");
        }

        tag.deleteTag();
        return "删除成功";
    }

    @Override
    public ResultPage<Demand> searchDemandByTagId(String id) {
        ResultPage<? extends Demand> rp = demandPersistent.search(
                ConditionUtil.and(
                        ConditionUtil.eq(ConditionUtil.field("tagId"), id), ConditionUtil.ne(ConditionUtil.field("status"), Demand.STATUS_DELETED.id)
                )
        );
        List<Demand> list = new ArrayList<>();
        for (Demand demand : ResultPageHelper.toForeach(rp)) {
            if (demand.getStatus().id == Demand.STATUS_DELETED.id) {
                continue;
            }
            list.add(demand);
        }
        return ResultPageHelper.toResultPage(list);
    }

    @Override
    public String follow(String id, String user) throws DemandException {
        Demand demand = demandPersistent.get(id);
        if (demand == null || demand.getStatus().id == Demand.STATUS_DELETED.id) {
            throw new DemandException("不存在该id任务");
        }
        demand.follow(user);
        return "跟进成功";
    }


    @Override
    public ResultPage<Bug> searchBugByDemandId(String id, String keyword) {
        ResultPage<? extends Bug> rp = bugPersister.search(
                ConditionUtil.and(
                        ConditionUtil.eq(ConditionUtil.field("demandId"), id), ConditionUtil.ne(ConditionUtil.field("status"), Bug.STATUS_DELETED.id)
                )
        );
        List<Bug> list = new ArrayList<>();
        for (Bug bug : ResultPageHelper.toForeach(rp)) {
            list.add(bug);
        }
        return ResultPageHelper.toResultPage(list);
    }

    @Override
    public List<Map<String, Integer>> analysis(String id) throws DemandException {
        Demand demand = demandPersistent.get(id);
        if (demand == null || demand.getStatus().id == Demand.STATUS_DELETED.id) {
            throw new DemandException("分析出错！不存在该id的任务");
        }

        ResultPage<? extends Bug> rp = bugPersister.search(
                ConditionUtil.and(
                        ConditionUtil.eq(ConditionUtil.field("demandId"), id), ConditionUtil.ne(ConditionUtil.field("status"), Bug.STATUS_DELETED.id)
                )
        );

        return demand.analysis(rp);
    }

    @Override
    public ResultPage<Bug> getAllBugs(String keywords) {
        ResultPage<? extends Bug> rp = bugPersister.startsWith("bug");
        List<Bug> list = new ArrayList<>();
        for (Bug bug : ResultPageHelper.toForeach(rp)) {

            if (bug.getStatus().id == Bug.STATUS_DELETED.id) {
                continue;
            }
            if (!bug.getDescription().contains(keywords)) {
                continue;
            }

            list.add(bug);
        }
        return ResultPageHelper.toResultPage(list);
    }


    private static boolean isMatch(Demand demand, String keywords) {

        if (null == demand) {
            return false;
        }
        String m_title = demand.getTitle();
        String m_creator = demand.getCreator();
        String m_follower = demand.getFollower();

        if (m_follower == null) {
            m_follower = "";
        }

        boolean result = false;
        for (String charger : demand.getDealer()) {
            if (charger.contains(keywords)) {
                result = true;
            }
        }
        return m_title.contains(keywords) || m_creator.contains(keywords) || m_follower.contains(keywords) || result;
    }

    private static boolean isMatch(Demand demand, int status) {

        NameItem m_status = demand.getStatus();

        if (status == OPTION_NONE) {
            return true;
        }
        if (status == OPTION_FINISHED) {
            if (m_status.id == Demand.STATUS_EVALUATING.id || m_status.id == Demand.STATUS_PLANNING.id || m_status.id == Demand.STATUS_ToBeDeveloped.id
                    || m_status.id == Demand.STATUS_DEVELOPING.id || m_status.id == Demand.STATUS_ToBeTested.id || m_status.id == Demand.STATUS_TESTING.id || m_status.id == Demand.STATUS_TESTED.id || m_status.id == Demand.STATUS_HANGED.id) {
                return false;
            }
        } else if (status == OPTION_NOTFINISHED) {
            if (m_status.id == Demand.STATUS_ONLINE.id || m_status.id == Demand.STATUS_REJECTED.id) {
                return false;
            }
        }
        return true;
    }

}
