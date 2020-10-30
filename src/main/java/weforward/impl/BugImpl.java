package weforward.impl;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import cn.weforward.data.UniteId;
import cn.weforward.data.annotation.Index;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.data.persister.support.AbstractPersistent;
import cn.weforward.framework.support.Global;
import weforward.Bug;
import weforward.di.DemandDi;
import weforward.exception.StatusException;

import javax.annotation.Resource;
import java.util.Date;

public class BugImpl extends AbstractPersistent<DemandDi> implements Bug {

    /** 任务id*/
    @Index
    @Resource
    protected String demandId;

    /** 缺陷具体描述*/
    @Resource
    protected String description;

    /** 缺陷严重性*/
    @Resource
    protected String priority;

    /** 缺陷状态*/
    @Resource
    protected int status;

    /** 缺陷是否处理完成，当缺陷状态为已解决、不做修改、无法解决时将状态置为已完成*/
    @Resource
    protected boolean isDealed;

    /** 缺陷测试人*/
    @Resource
    protected String tester;

    /** 缺陷处理人*/
    @Resource
    protected String dealer;

    /** 缺陷跟进人*/
    @Resource
    protected String follower;

    /** 缺陷创建人*/
    @Resource
    protected String creator;

    /** 缺陷版本与平台*/
    @Resource
    protected String version;

    /** 最后一次更新时间，每次更新操作或状态扭转刷新该时间*/
    @Resource
    protected Date lastTime;


    protected BugImpl(DemandDi di) {
        super(di);
    }


    public BugImpl(DemandDi di, String user, String demandId, String description, String priority, String dealer, String version) {
        super(di);
        genPersistenceId();
        this.demandId = demandId;
        this.description = description;
        this.priority = priority;
        this.status = STATUS_ToBeCorrected.id;
        this.isDealed = false;
        this.tester = user;
        this.dealer = dealer;
        this.creator = user;
        this.version = version;
        this.lastTime = new Date(System.currentTimeMillis());
        markPersistenceUpdate();
        getBusinessDi().writeLog(getId(),"创建了一个新的BUG", "", "");
    }

    @Override
    public UniteId getId() {
        return getPersistenceId();
    }

    @Override
    public String getDemandId() {
        return demandId;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
        markPersistenceUpdate();
    }

    @Override
    public String getPriority() {
        return this.priority;
    }

    @Override
    public void setPriority(String priority) {
        this.priority = priority;
        markPersistenceUpdate();
    }

    @Override
    public NameItem getStatus() {
        return STATUS.get(this.status);
    }

    @Override
    public boolean isDealed() {
        return isDealed;
    }

    @Override
    public String getTester() {
        return tester;
    }

    @Override
    public void setTester(String tester) {
        this.tester = tester;
        markPersistenceUpdate();
    }

    @Override
    public String getDealer() {
        return dealer;
    }

    @Override
    public void setDealer(String dealer) {
        this.dealer = dealer;
        markPersistenceUpdate();
    }

    @Override
    public String getCreator() {
        return creator;
    }

    @Override
    public Date getLastTime() {
        return lastTime;
    }

    @Override
    public void follow(String follower){
        this.follower = follower;
    }


    @Override
    public void toBeCorrected() throws StatusException {

        if (this.status != STATUS_ToBeRested.id && this.status != STATUS_SUGGEST.id && this.status != STATUS_APPLY.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_ToBeCorrected.getName());
        }
        this.status = STATUS_ToBeCorrected.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_ToBeCorrected.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        markPersistenceUpdate();
    }

    @Override
    public void toBeRested() throws StatusException {

        if (this.status != STATUS_ToBeCorrected.id && this.status != STATUS_REOPENED.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_ToBeRested.getName());
        }
        this.status = STATUS_ToBeRested.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_ToBeRested.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        markPersistenceUpdate();
    }

    @Override
    public void toSuggest() throws StatusException {

        if (this.status != STATUS_ToBeCorrected.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_SUGGEST.getName());
        }
        this.status = STATUS_SUGGEST.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_SUGGEST.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        markPersistenceUpdate();
    }

    @Override
    public void toApply() throws StatusException {

        if (this.status != STATUS_ToBeCorrected.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_APPLY.getName());
        }
        this.status = STATUS_APPLY.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_APPLY.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        markPersistenceUpdate();
    }

    @Override
    public void toDone() throws StatusException {

        if (this.status != STATUS_ToBeRested.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_DONE.getName());
        }
        this.status = STATUS_DONE.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_DONE.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        this.isDealed = true;
        markPersistenceUpdate();
    }

    @Override
    public void toNone() throws StatusException {

        if (this.status != STATUS_SUGGEST.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_NONE.getName());
        }
        this.status = STATUS_DONE.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_NONE.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        this.isDealed = true;
        markPersistenceUpdate();
    }

    @Override
    public void toCant() throws StatusException {

        if (this.status != STATUS_APPLY.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_CANT.getName());
        }
        this.status = STATUS_CANT.id;
        getBusinessDi().writeLog(getId(), "状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_CANT.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        this.isDealed = true;
        markPersistenceUpdate();
    }

    @Override
    public void toReopened() throws StatusException {
        if (this.status != STATUS_DONE.id && this.status != STATUS_NONE.id && this.status != STATUS_CANT.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_REOPENED.getName());
        }
        this.status = STATUS_REOPENED.id;
        getBusinessDi().writeLog(getId(),"状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_REOPENED.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        this.isDealed = false;
        markPersistenceUpdate();
    }

    @Override
    public ResultPage<BusinessLog> getLogs() {
        return getBusinessDi().getLogs(getId());
    }
}
