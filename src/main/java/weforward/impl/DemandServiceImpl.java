package weforward.impl;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.data.log.BusinessLoggerFactory;
import cn.weforward.data.persister.PersisterFactory;
import cn.weforward.data.persister.ext.ConditionUtil;
import cn.weforward.framework.ApiException;
import weforward.Bug;
import weforward.Demand;
import weforward.exception.DemandException;
import weforward.exception.TagException;
import weforward.Tag;
import weforward.DemandService;
import weforward.view.DemandAnalysisView;

import java.util.*;

public class DemandServiceImpl extends DemandDiImpl implements DemandService {

    public DemandServiceImpl(PersisterFactory factory, BusinessLoggerFactory loggerFactory) {
        super(factory, loggerFactory);
    }

    @Override
    public Demand createDemand(String user, String title, String description, int priority, String charger, Set<String> dealer, Date willingStartTime, Date willingEndTime, String tagId) throws TagException {

        if(tagId !=null && tagId != ""){
            getTag(tagId);
        }
        return new DemandImpl(this, user, title, description, priority, charger, dealer, willingStartTime, willingEndTime, tagId);
    }

    @Override
    public Demand createSonDemand(String user, String fid, String title, String description, int priority, String charger, Set<String> dealer, Date willingStartTime, Date willingEndTime, String tagId) throws DemandException, TagException {

        Demand demand = getDemand(fid);
        if(demand.getFid() != null){
            throw new DemandException("新增子任务失败！该id的任务不为父任务");
        }

        if(tagId!="" || tagId == null){
            Tag tag = getTag(tagId);
        }
        demand.writeSonLog();
        return new DemandImpl(this, user, fid, title, description, priority, charger, dealer, willingStartTime, willingEndTime);
    }

    @Override
    public Bug createBug(String user, String demandId, String description, String priority, String dealer, String version) throws DemandException {
        Demand demand = getDemand(demandId);
        return demand.createBug(this, user, demand.getId().getOrdinal(), description, priority, dealer, version);
    }

    @Override
    public Tag createTag(String name) {
        return new TagImpl(this, name);
    }

    @Override
    public Bug getBug(String id) throws ApiException {
        Bug bug =  bugPersister.get(id);
        if(bug == null ){
            throw new ApiException(ApiException.CODE_INTERNAL_ERROR,"没有该id的缺陷");
        }
        return bug;
    }

    @Override
    public Demand getDemand(String id) throws DemandException {
        Demand demand = demandPersistent.get(id);
        if(demand ==null || demand.getStatus().id == Demand.STATUS_DELETED.id){
            throw new DemandException("获取任务失败！不存在该id的任务");
        }
        return demand;
    }

    @Override
    public Tag getTag(String id) throws TagException {
        Tag tag = tagPersister.get(id);
        if(tag == null || tag.getStatus().id == Tag.STATUS_DELETED.id){
            throw new TagException("获取标签失败！不存在该id的标签");
        }
        return tag;
    }

    @Override
    public Demand deleteDemand(String id) throws DemandException {
        Demand demand = getDemand(id);
        demand.delete();
        return demand;
    }

    @Override
    public ResultPage<Demand> searchDemands(String keywords, String creator, String follower, String dealer, String charger,int option) throws DemandException {

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
            if (!isMatch(demand, keywords, creator,follower, dealer, charger)) {
                continue;
            }
            list.add(demand);
        }
        return ResultPageHelper.toResultPage(list);
    }

    @Override
    public void addTagForDemandByTagId(String demandId, String tagId) throws TagException, DemandException {
        Demand demand = getDemand(demandId);
        Tag tag = getTag(tagId);

        demand.addTagForDemand(tagId);
        return ;
    }

    @Override
    public void dropTagForDemandByTagId(String demandId) throws TagException, DemandException {

        Demand demand = getDemand(demandId);

        if(demand.getTagId()==null || demand.getTagId().equals("")){
            throw new TagException("任务的标签为空，不能删除标签");
        }

        demand.dropTagForDemand(demandId);
        return ;
    }

    @Override
    public ResultPage <Demand> searchSonDemand(String id) throws DemandException {

        Demand demand = getDemand(id);

        ResultPage<? extends Demand> rp = demandPersistent.search(
                ConditionUtil.and(
                        ConditionUtil.eq(ConditionUtil.field("fid"), id), ConditionUtil.ne(ConditionUtil.field("status"), Demand.STATUS_DELETED.id)
                )
        );

        return (ResultPage<Demand>) rp;
    }


    @Override
    public ResultPage<Tag> searchTagByKeywords(String keywords) {
        ResultPage<? extends Tag> rp = tagPersister.search(
                ConditionUtil.ne(ConditionUtil.field("status"),Tag.STATUS_DELETED.id)
        );
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
    public void deleteTag(String id) throws TagException {

        Tag tag = getTag(id);

        ResultPage<? extends Demand> rp = demandPersistent.search(
                ConditionUtil.and(
                        ConditionUtil.eq(ConditionUtil.field("tagId"), id), ConditionUtil.ne(ConditionUtil.field("status"), Demand.STATUS_DELETED.id)
                )
        );

        if (rp.getCount() != 0) {
            throw new TagException("还有需求正在使用本标签，不能删除");
        }

        tag.deleteTag();
        return ;
    }

    @Override
    public ResultPage<Demand> searchDemandByTagId(String id) throws TagException {

        Tag tag = getTag(id);

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
    public void followDemand(String id, String user) throws DemandException {
        Demand demand = getDemand(id);
        demand.follow(user);
        return ;
    }

    @Override
    public void followBug(String id, String user) throws  ApiException {
        Bug bug = getBug(id);
        bug.follow(user);
        return ;
    }


    @Override
    public ResultPage<Bug> searchBugByDemandId(String id, String keyword, String k_tester, String k_dealer, int option) {
        ResultPage<? extends Bug> rp = bugPersister.search(
                ConditionUtil.and(
                        ConditionUtil.eq(ConditionUtil.field("demandId"), id), ConditionUtil.ne(ConditionUtil.field("status"), Bug.STATUS_DELETED.id)
                )
        );
        List<Bug> list = new ArrayList<>();
        for (Bug bug : ResultPageHelper.toForeach(rp)) {
            if(!isMatch(bug, keyword, k_tester, k_dealer, option)){
                continue;
            }
            list.add(bug);
        }
        return ResultPageHelper.toResultPage(list);
    }



    @Override
    public DemandAnalysisView analysis(String id) throws DemandException {
        Demand demand = getDemand(id);

        ResultPage<? extends Bug> rp = bugPersister.search(
                ConditionUtil.and(
                        ConditionUtil.eq(ConditionUtil.field("demandId"), id), ConditionUtil.ne(ConditionUtil.field("status"), Bug.STATUS_DELETED.id)
                )
        );

        return demand.analysis(rp);
    }



    private static boolean isMatch(Demand demand, String keywords, String creator, String follower, String dealer, String charger) {

        boolean flag = false;

        if (null == demand) {
            return false;
        }
        String m_title = demand.getTitle();
        String m_creator = demand.getCreator();
        Set<String> m_follower = demand.getFollower();
        String m_charger = demand.getCharger();

        Iterator iterator = m_follower.iterator();
        if(m_follower.contains(follower)){
            flag = true;
        }

        for (String m_dealer : demand.getDealer()) {
            if (m_dealer.equals(dealer)) {
                flag = true;
            }
        }
        if(!StringUtil.isEmpty(creator) && !StringUtil.isEmpty(follower) && !StringUtil.isEmpty(charger) && !StringUtil.isEmpty(dealer)){
            return m_title.contains(keywords) && m_creator.equals(creator) && m_follower.equals(follower) && m_charger.equals(charger) && flag;
        }

        if(!StringUtil.isEmpty(creator) && !StringUtil.isEmpty(follower) && !StringUtil.isEmpty(charger)){
            return m_title.contains(keywords) && m_creator.equals(creator) && m_follower.equals(follower) && m_charger.equals(charger);
        }

        if(!StringUtil.isEmpty(creator) && !StringUtil.isEmpty(charger) && !StringUtil.isEmpty(dealer)){
            return m_title.contains(keywords) && m_creator.equals(creator) && m_charger.equals(charger) && flag;
        }

        if( !StringUtil.isEmpty(creator) && !StringUtil.isEmpty(follower) && !StringUtil.isEmpty(dealer)){
            return m_title.contains(keywords) && m_creator.equals(creator)  && m_charger.equals(follower) && flag;
        }

        if( !StringUtil.isEmpty(follower) && !StringUtil.isEmpty(charger) && !StringUtil.isEmpty(dealer)){
            return m_title.contains(keywords) && m_follower.equals(follower) && m_charger.equals(charger) && flag;
        }

        if(!StringUtil.isEmpty(creator) && !StringUtil.isEmpty(follower) ){
            return m_title.contains(keywords) && m_creator.equals(creator) && m_follower.equals(follower);
        }

        if(!StringUtil.isEmpty(creator) && !StringUtil.isEmpty(charger) ){
            return m_title.contains(keywords) && m_creator.equals(creator) && m_follower.equals(charger);
        }

        if(!StringUtil.isEmpty(creator) && !StringUtil.isEmpty(dealer) ){
            return m_title.contains(keywords) && m_creator.equals(creator) && flag;
        }

        if(!StringUtil.isEmpty(follower) && !StringUtil.isEmpty(charger) ){
            return m_title.contains(keywords) && m_creator.equals(follower) && m_follower.equals(charger);
        }

        if(!StringUtil.isEmpty(follower) && !StringUtil.isEmpty(dealer) ){
            return m_title.contains(keywords) && m_follower.equals(follower) && flag;
        }

        if(!StringUtil.isEmpty(charger) && !StringUtil.isEmpty(dealer) ){
            return m_title.contains(keywords) && m_follower.equals(charger) && flag;
        }

        if(!StringUtil.isEmpty(creator) ){
            return m_title.contains(keywords) && m_creator.equals(creator);
        }

        if(!StringUtil.isEmpty(follower) ){
            return m_title.contains(keywords) && m_follower.equals(follower);
        }

        if(!StringUtil.isEmpty(charger) ){
            return m_title.contains(keywords) && m_charger.equals(charger);
        }

        if(!StringUtil.isEmpty(dealer) ){
            return m_title.contains(keywords) && flag;
        }

        return m_title.contains(keywords);
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

    private static boolean isMatch(Bug bug, String keyword, String k_tester, String k_dealer, int option){

        String description = bug.getDescription();
        String dealer = bug.getDealer();
        String tester = bug.getTester();


        int status = bug.getStatus().id;
        if(option == OPTION_FINISHED){
            if(status != Bug.STATUS_CANT.id && status != Bug.STATUS_DONE.id && status != Bug.STATUS_NONE.id){
                return false;
            }
        }
        if(option == OPTION_NOTFINISHED){
            if(status == Bug.STATUS_CANT.id || status == Bug.STATUS_DONE.id || status == Bug.STATUS_NONE.id){
                return false;
            }
        }

        if(!StringUtil.isEmpty(k_dealer) && !StringUtil.isEmpty(k_tester))
        return description.contains(keyword) && dealer.equals(k_dealer) && tester.equals(k_tester) ;

        if(!StringUtil.isEmpty(k_dealer))
        return description.contains(keyword) && dealer.equals(k_dealer) ;

        if(!StringUtil.isEmpty(k_tester))
        return description.contains(keyword) && tester.equals(k_tester) ;

        return description.contains(keyword);
    }

}
