package weforward.Impl;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.data.log.BusinessLoggerFactory;
import cn.weforward.data.persister.PersisterFactory;
import cn.weforward.data.persister.ext.ConditionUtil;
import cn.weforward.framework.support.Global;
import weforward.Bug;
import weforward.Demand;
import weforward.Exception.TagException;
import weforward.Tag;
import weforward.Exception.StatusException;
import weforward.DemandService;

import java.util.*;

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
    public Bug createBug(String user, String demandId, String description, String priority , String dealer , String version){
        return new BugImpl(this,user,demandId,description,priority,dealer,version);
    }

    @Override
    public Tag createTag(String name) {
        return new TagImpl(this,name);
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
    public Demand deleteDemand(String id) {
        Demand demand = demandPersistent.get(id);
        demand.delete();
        return demand;
    }

    @Override
    public ResultPage<Demand> searchDemands(String keywords, int option) {

        ResultPage<? extends Demand> rp = demandPersistent.search(
                ConditionUtil.and(
                        ConditionUtil.eq(ConditionUtil.field("fid"), (String) null), ConditionUtil.ne(ConditionUtil.field("status"), Demand.STATUS_DELETED.id)
        ));

        List<Demand> list = new ArrayList<>();
        for (Demand demand : ResultPageHelper.toForeach(rp)) {
            if (!isMatch(demand, keywords)) {
                continue;
            }
            if (!isMatch(demand, option)) {
                continue;
            }
            if(demand.getStatus().id == Demand.STATUS_DELETED.id){
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
    public ResultPage<Tag> searchTagByKeywords(String keywords) {
        ResultPage<? extends Tag> rp = tagPersister.startsWith("tag");
        List<Tag> list = new ArrayList<>();
        // 产品不多的时候直接遍历过滤，多了就要考虑使用索引查询
        for (Tag tag : ResultPageHelper.toForeach(rp)) {

            if(tag.getStatus().id == Tag.STATUS_DELETED.id){
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
                ConditionUtil.and(
                        ConditionUtil.eq(ConditionUtil.field("tagId"), id),ConditionUtil.ne(ConditionUtil.field("status"),2)
                )
        );

        if(rp != null && rp.hasNext()){
            throw new TagException("还有需求正在使用本标签，不能删除");
        }

        if(tagPersister.get(id).getStatus().id == Tag.STATUS_DELETED.id){
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
            if(demand.getStatus().id == Demand.STATUS_DELETED.id){
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


    @Override
    public ResultPage<Bug> searchBugByDemandId(String id) {
        ResultPage<? extends Bug> rp = bugPersister.search(
                ConditionUtil.eq(ConditionUtil.field("demandId"),id)
        );

        List<Bug> list = new ArrayList<>();
        for (Bug bug : ResultPageHelper.toForeach(rp)) {
            if(bug.getStatus().id == Bug.STATUS_DELETED.id){
                continue;
            }
            list.add(bug);
        }
        return ResultPageHelper.toResultPage(list);
    }

    @Override
    public List <Map<String,Integer>> analysis(String id) {

        ResultPage<? extends Bug> rp = bugPersister.search(
                ConditionUtil.eq(ConditionUtil.field("demandId"),id)
        );

        List<Map<String,Integer>> list = new ArrayList<>();
        HashMap <String,Integer> finishMap = new HashMap<>();
        HashMap <String,Integer> statusMap = new HashMap<>();
        HashMap <String,Integer> testerMap = new HashMap<>();
        HashMap <String,Integer> dealerMap = new HashMap<>();

        for (Bug bug : ResultPageHelper.toForeach(rp)) {

            if(bug.getStatus().id == Bug.STATUS_DELETED.id){
                continue;
            }

            if(bug.isDealed() == true){
                if( finishMap.get("已完成") ==null){
                    finishMap.put("已完成",1);
                }
                else{
                    finishMap.put("已完成",finishMap.get("已完成")+1);
                }
            }

            if(bug.isDealed() == false){
                if( finishMap.get("未完成") ==null){
                    finishMap.put("未完成",1);
                }
                else{
                    finishMap.put("未完成",finishMap.get("未完成")+1);
                }
            }

            if(bug.getStatus().id == Bug.STATUS_ToBeCorrected.id){
                if( statusMap.get("待修正") ==null){
                    statusMap.put("待修正",1);
                }
                else{
                    statusMap.put("待修正",statusMap.get("待修正")+1);
                }
            }

            if(bug.getStatus().id == Bug.STATUS_ToBeRested.id){
                if( statusMap.get("待复测") ==null){
                    statusMap.put("待复测",1);
                }
                else{
                    statusMap.put("待复测",statusMap.get("待复测")+1);
                }
            }

            if(bug.getStatus().id == Bug.STATUS_SUGGEST.id){
                if( statusMap.get("建议不做修改") ==null){
                    statusMap.put("建议不做修改",1);
                }
                else{
                    statusMap.put("建议不做修改",statusMap.get("建议不做修改")+1);
                }
            }

            if(bug.getStatus().id == Bug.STATUS_APPLY.id){
                if( statusMap.get("申请无法解决") ==null){
                    statusMap.put("申请无法解决",1);
                }
                else{
                    statusMap.put("申请无法解决",statusMap.get("申请无法解决")+1);
                }
            }

            if(bug.getStatus().id == Bug.STATUS_DONE.id){
                if( statusMap.get("已解决") ==null){
                    statusMap.put("已解决",1);
                }
                else{
                    statusMap.put("已解决",statusMap.get("已解决")+1);
                }
            }

            if(bug.getStatus().id == Bug.STATUS_NONE.id){
                if( statusMap.get("不做修改") ==null){
                    statusMap.put("不做修改",1);
                }
                else{
                    statusMap.put("不做修改",statusMap.get("不做修改")+1);
                }
            }

            if(bug.getStatus().id == Bug.STATUS_CANT.id){
                if( statusMap.get("无法解决") ==null){
                    statusMap.put("无法解决",1);
                }
                else{
                    statusMap.put("无法解决",statusMap.get("无法解决")+1);
                }
            }

            if(bug.getStatus().id == Bug.STATUS_REOPENED.id){
                if( statusMap.get("重新打开") ==null){
                    statusMap.put("重新打开",1);
                }
                else{
                    statusMap.put("重新打开",statusMap.get("重新打开")+1);
                }
            }

            String tester = bug.getTester();
            if( testerMap.get(tester) ==null){
                testerMap.put(tester,1);
            }
            else{
                testerMap.put(tester,testerMap.get(tester)+1);
            }

            String dealer = bug.getDealer();
            if( dealerMap.get(dealer) ==null){
                dealerMap.put(dealer,1);
            }
            else{
                dealerMap.put(dealer,dealerMap.get(dealer)+1);
            }
        }
        list.add(finishMap);
        list.add(statusMap);
        list.add(testerMap);
        list.add(dealerMap);
        return list;
    }

    @Override
    public ResultPage<Bug> getAllBugs(String keywords) {
        ResultPage<? extends Bug> rp = bugPersister.startsWith("bug");
        List<Bug> list = new ArrayList<>();
        // 产品不多的时候直接遍历过滤，多了就要考虑使用索引查询
        for (Bug bug : ResultPageHelper.toForeach(rp)) {

            if(bug.getStatus().id == Bug.STATUS_DELETED.id){
                continue;
            }
            if(!bug.getDescription().contains(keywords)){
                continue;
            }

            list.add(bug);
        }
        return ResultPageHelper.toResultPage(list);
    }



    private static boolean isMatch(Demand demand, String keywords) {
        if (null == demand || demand.getStatus().id==Demand.STATUS_DELETED.id) {
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

        NameItem m_status = demand.getStatus();

        if(status == OPTION_NONE){
            return true;
        }
        if (status == OPTION_FINISHED) {
            if (m_status.id == Demand.STATUS_EVALUATING.id ||  m_status.id == Demand.STATUS_PLANNING.id || m_status.id == Demand.STATUS_ToBeDeveloped.id
                    || m_status.id == Demand.STATUS_DEVELOPING.id || m_status.id == Demand.STATUS_ToBeTested.id || m_status.id == Demand.STATUS_TESTING.id || m_status.id == Demand.STATUS_TESTED.id|| m_status.id == Demand.STATUS_HANGED.id) {
                return false;
            }
        } else if (status == OPTION_NOTFINISHED) {
            if (m_status.id == Demand.STATUS_ONLINE.id || m_status.id == Demand.STATUS_REJECTED.id ) {
                return false;
            }
        }
        return true;
    }

}
