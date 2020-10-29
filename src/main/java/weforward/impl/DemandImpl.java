package weforward.impl;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import cn.weforward.data.UniteId;
import cn.weforward.data.annotation.Index;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.data.persister.support.AbstractPersistent;
import cn.weforward.framework.ApiException;
import weforward.Bug;
import weforward.Demand;
import weforward.di.DemandDi;
import weforward.exception.StatusException;
import weforward.exception.TagException;
import weforward.view.DemandAnalysisView;

import javax.annotation.Resource;
import java.util.*;

public class DemandImpl  extends AbstractPersistent<DemandDi> implements Demand {

    /*任务父任务Id*/
    @Resource
    @Index
    protected String fid;

    /*任务标题*/
    @Resource
    protected String title;

    /*任务具体描述*/
    @Resource
    protected String description;

    /*任务优先级 1-最高级 2-高级 3-中级 4-低级 */
    @Resource
    protected int priority;

    /*任务处理人*/
    @Resource
    protected Set<String> dealer = new HashSet<>();

    /*任务负责人*/
    @Resource
    protected String charger;

    /*任务跟进人*/
    @Resource
    protected Set<String> follower = new HashSet<>();

    /*任务预期开始时间*/
    @Resource
    protected Date willingStartTime;

    /*任务预期结束时间*/
    @Resource
    protected Date willingEndTime;

    /*任务结束时间*/
    @Resource
    protected Date endTime;

    /*任务创建时间*/
    @Resource
    protected Date createTime;

    /*任务当前状态*/
    @Resource
    protected int status;

    /*任务创建人名称*/
    @Resource
    protected String creator;

    /*所属标签Id*/
    @Resource
    @Index
    protected String tagId;

    protected List <String> whichStatusCanTurnto = new ArrayList<>();

    protected DemandImpl(DemandDi di) {
        super(di);
    }

    /**
     * 普通任务创建构造函数
     * @param di
     * @param user
     * @param title
     * @param description
     * @param priority
     * @param charger
     * @param dealer
     * @param willingStartTime
     * @param willingEndTime
     * @param tagId
     * @return
     */
    public DemandImpl(DemandDi di, String user, String title, String description, int priority, String charger, Set<String> dealer,
                      Date willingStartTime, Date willingEndTime, String tagId) {
        super(di);
        genPersistenceId();
        this.title = title;
        this.description = description;
        this.priority = PRIORITY.get(priority).id;
        this.charger = charger;
        this.willingStartTime = willingStartTime;
        this.willingEndTime = willingEndTime;
        this.status = STATUS_EVALUATING.id;
        this.createTime = new Date();
        this.creator = user;
        this.fid = null;
        this.tagId = tagId;
        if(dealer.size() != 0){
            this.dealer = dealer;
        }
        markPersistenceUpdate();
        getBusinessDi().writeLog(getId(), "创建了一个新的任务", "", "");
    }

    /**
     * 带父id参数的构造函数，用于创建子任务
     * @param di
     * @param fid
     * @param title
     * @param description
     * @param priority
     * @param willingStartTime
     * @param willingEndTime
     * @return
     */

    public DemandImpl(DemandDi di, String user, String fid, String title, String description, int priority, String charger, Set<String> dealer,
                      Date willingStartTime, Date willingEndTime) {
        super(di);
        genPersistenceId();
        this.fid = fid;
        this.title = title;
        this.description = description;
        this.priority = PRIORITY.get(priority).id;
        this.charger = charger;
        this.willingStartTime = willingStartTime;
        this.willingEndTime = willingEndTime;
        this.status = STATUS_EVALUATING.id;
        this.createTime = new Date();
        this.creator = user;
        this.dealer = dealer;
        markPersistenceUpdate();
        getBusinessDi().writeLog(getId(), "创建了一个新的子任务", "", "");
    }

    @Override
    public UniteId getId() {
        return getPersistenceId();
    }

    public void writeSonLog() {
        getBusinessDi().writeLog(getId(), "创建了一个新的子任务", "", "");
    }

    @Override
    public String getFid() {
        return fid;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        if(this.title.equals(title)) return;
        getBusinessDi().writeLog(getId(), "修改标题", "标题从 "+this.title +" 修改为："+title, "");
        this.title = title;
        markPersistenceUpdate();
    }

    @Override
    public String getCharger() {
        return this.charger;
    }

    @Override
    public void setCharger(String charger) {
        if(this.charger.equals(charger)) return;
        getBusinessDi().writeLog(getId(), "修改负责人", "负责人从 "+this.charger +" 修改为："+charger, "");
        this.charger = charger;
        markPersistenceUpdate();
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        if(this.description.equals(description)) return;
        getBusinessDi().writeLog(getId(), "修改具体描述", "原具体描述：  "+this.description +" \n 修改为："+description, "");
        this.description = description;
        markPersistenceUpdate();
    }

    @Override
    public Set<String> getDealer() {
        return this.dealer;
    }

    @Override
    public void setDealer(Set<String> dealer) {
        if(this.dealer.toString().equals(dealer.toString())) return;
        getBusinessDi().writeLog(getId(), "修改处理人", "原处理人：  "+this.dealer +"\n修改为："+dealer, "");
        this.dealer = dealer;
        markPersistenceUpdate();
    }

    @Override
    public Set<String> getFollower() {
        return this.follower;
    }

    @Override
    public void follow(String follower) {
        int size = this.follower.size();
        this.follower.add(follower);
        if(this.follower.size() != size){
            getBusinessDi().writeLog(getId(), "跟进人添加", "添加了新的跟进人：  "+follower, "");
            markPersistenceUpdate();
        }
    }

    public void disFollow(String follower) throws ApiException {
        if(!this.follower.contains(follower)){
            throw new ApiException(ApiException.CODE_INTERNAL_ERROR,"该用户还没有跟进，不能取消跟进！");
        }
        Iterator iterator = this.follower.iterator();
        while(iterator.hasNext()){
            if(iterator.equals(follower)){
                iterator.remove();
                break;
            }
        }
        getBusinessDi().writeLog(getId(), "跟进人移除", "跟进人：  "+follower+" 不再跟进", "");
        markPersistenceUpdate();
    }

    @Override
    public Date getWillingStartTime() {
        return this.willingStartTime;
    }

    @Override
    public void setWillingStartTime(Date willingStartTime) {
        if(this.willingStartTime.equals(willingStartTime)) return;
        getBusinessDi().writeLog(getId(), "修改预期开始时间", "原预期开始时间：  "+this.willingStartTime +"  修改为："+willingStartTime, "");
        this.willingStartTime = willingStartTime;
        markPersistenceUpdate();
    }

    @Override
    public Date getWillingEndTime() {
        return this.willingEndTime;
    }

    @Override
    public void setWillingEndTime(Date willingEndTime) {
        if(this.willingEndTime.equals(willingEndTime)) return;
        getBusinessDi().writeLog(getId(), "修改预期结束时间", "原预期结束时间：  "+this.willingEndTime +"  修改为："+willingEndTime, "");
        this.willingEndTime = willingEndTime;
        markPersistenceUpdate();
    }

    @Override
    public Date getEndTime() {
        return this.endTime;
    }

    @Override
    public Date getCreateTime() {
        return this.createTime;
    }

    @Override
    public String getCreator() {
        return this.creator;
    }

    @Override
    public NameItem getStatus() {
        return STATUS.get(status);
    }

    @Override
    public NameItem getPriority() {
        return PRIORITY.get(priority);
    }

    @Override
    public void setPriority(int priority) {
        if(this.priority == priority) return;
        getBusinessDi().writeLog(getId(), "修改优先级", "原优先级：  "+ PRIORITY.get(this.priority).getName() +"  修改为："+PRIORITY.get(priority).getName(), "");
        this.priority = priority;
        markPersistenceUpdate();
    }

    @Override
    public String getTagId() {
        return tagId;
    }

    @Override
    public void setTagId(String tagId) {
        if(this.tagId == tagId){
            return ;
        }
        this.tagId = tagId;
        markPersistenceUpdate();
    }


    public void toEvaluating() throws StatusException {

        if(STATUS.get(this.status).id == STATUS_EVALUATING.id){
            return ;
        }

        if(this.status != STATUS_HANGED.id) {
            throw new StatusException(STATUS.get(this.status).getName()+"不能扭转为"+STATUS_EVALUATING.getName());
        }
        this.status = STATUS_EVALUATING.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_EVALUATING.getName(), "");
        markPersistenceUpdate();
    }

    public void toPlanning() throws StatusException {

        if(STATUS.get(this.status).id == STATUS_PLANNING.id){
            return ;
        }

        if(this.status != STATUS_EVALUATING.id) {
            throw new StatusException(STATUS.get(this.status).getName()+"不能扭转为"+STATUS_PLANNING.getName());
        }
        this.status = STATUS_PLANNING.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_PLANNING.getName(), "");
        markPersistenceUpdate();
    }

    public void toBeDeveloped() throws StatusException {

        if(STATUS.get(this.status).id == STATUS_ToBeDeveloped.id){
            return ;
        }

        if(this.status != STATUS_PLANNING.id && this.status != STATUS_HANGED.id) {
            throw new StatusException(STATUS.get(this.status).getName()+"不能扭转为"+STATUS_ToBeDeveloped.getName());
        }
        this.status = STATUS_ToBeDeveloped.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_ToBeDeveloped.getName(), "");
        markPersistenceUpdate();
    }

    public void toDevloping() throws StatusException {

        if(STATUS.get(this.status).id == STATUS_DEVELOPING.id){
            return ;
        }

        if(this.status != STATUS_ToBeDeveloped.id && this.status != STATUS_ToBeTested.id && this.status != STATUS_TESTING.id && this.status != STATUS_TESTED.id) {
            throw new StatusException(STATUS.get(this.status).getName()+"不能扭转为"+STATUS_DEVELOPING.getName());
        }
        this.status = STATUS_DEVELOPING.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_DEVELOPING.getName(), "");
        markPersistenceUpdate();
    }

    public void toBeTested() throws StatusException {

        if(STATUS.get(this.status).id == STATUS_ToBeTested.id){
            return ;
        }

        if(this.status != STATUS_DEVELOPING.id && this.status != STATUS_HANGED.id ) {
            throw new StatusException(STATUS.get(this.status).getName()+"不能扭转为"+STATUS_ToBeTested.getName());
        }
        this.status = STATUS_ToBeTested.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_ToBeTested.getName(), "");
        markPersistenceUpdate();
    }

    public void toTesting() throws StatusException {

        if(STATUS.get(this.status).id == STATUS_TESTING.id){
            return ;
        }

        if(this.status != STATUS_ToBeTested.id ) {
            throw new StatusException(STATUS.get(this.status).getName()+"不能扭转为"+STATUS_TESTING.getName());
        }
        this.status = STATUS_TESTING.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_TESTING.getName(), "");
        markPersistenceUpdate();
    }

    public void toTested() throws StatusException {

        if(STATUS.get(this.status).id == STATUS_TESTED.id){
            return ;
        }

        if(this.status != STATUS_TESTING.id && this.status != STATUS_HANGED.id) {
            throw new StatusException(STATUS.get(this.status).getName()+"不能扭转为"+STATUS_TESTED.getName());
        }
        this.status = STATUS_TESTED.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_TESTED.getName(), "");
        markPersistenceUpdate();
    }

    public void toOnline() throws StatusException {

        if(STATUS.get(this.status).id == STATUS_ONLINE.id){
            return ;
        }

        if(this.status != STATUS_TESTED.id) {
            throw new StatusException(STATUS.get(this.status).getName()+"不能扭转为"+STATUS_ONLINE.getName());
        }
        this.status = STATUS_ONLINE.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_ONLINE.getName(), "");
        this.endTime = new Date();
        markPersistenceUpdate();
    }

    public void toRejected() throws StatusException {

        if(STATUS.get(this.status).id == STATUS_REJECTED.id){
            return ;
        }

        if(this.status != STATUS_EVALUATING.id && this.status != STATUS_PLANNING.id && this.status != STATUS_ToBeDeveloped.id && this.status != STATUS_DEVELOPING.id
                && this.status != STATUS_ToBeTested.id&&  this.status != STATUS_TESTING.id && this.status != STATUS_TESTED.id && this.status != STATUS_HANGED.id)  {
            throw new StatusException(STATUS.get(this.status).getName()+"不能扭转为"+STATUS_REJECTED.getName());
        }
        this.status = STATUS_REJECTED.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_REJECTED.getName(), "");
        this.endTime = new Date();
        markPersistenceUpdate();
    }

    public void toHanged() throws StatusException {

        if(STATUS.get(this.status).id == STATUS_HANGED.id){
            return ;
        }

        if(this.status != STATUS_PLANNING.id && this.status != STATUS_ToBeDeveloped.id && this.status != STATUS_DEVELOPING.id && this.status != STATUS_ToBeTested.id
                &&  this.status != STATUS_TESTING.id && this.status != STATUS_TESTED.id) {
            throw new StatusException(STATUS.get(this.status).getName()+"不能扭转为"+STATUS_HANGED.getName());
        }
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_HANGED.getName(), "");
        this.status = STATUS_HANGED.id;
        this.endTime = new Date();
        markPersistenceUpdate();
    }

    /**
     * 逻辑删除一个任务
     * @param
     * @return
     */
    @Override
    public void delete() {
        getBusinessDi().writeLog(getId(), "任务删除", "任务被删除", "");
        this.status = STATUS_DELETED.id;
        this.tagId = null;
        markPersistenceUpdate();
    }

    @Override
    public void addTagForDemand(String tagId){
        getBusinessDi().writeLog(getId(), "修改所属标签", "原标签id： "+ this.tagId +"  修改为："+ tagId, "");
        this.tagId = null;
    }


    @Override
    public void dropTagForDemand(String demandId) throws TagException {
        getBusinessDi().writeLog(getId(), "删除所属标签", "原标签id： "+ this.tagId +" 被清空", "");
        this.tagId = null;
    }

    @Override
    public Bug createBug(DemandDi di, String user, String demandId, String description, String priority, String dealer, String version){
        return getBusinessDi().createBug(di, user, demandId, description, priority, dealer, version);
    }

    @Override
    public DemandAnalysisView analysis(ResultPage<? extends Bug> rp){
        return getBusinessDi().analysis(rp);
    }

    @Override
    public ResultPage<BusinessLog> getLogs() {
        return getBusinessDi().getLogs(getId());
    }

}
