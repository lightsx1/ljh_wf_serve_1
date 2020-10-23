package weforward.Impl;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import cn.weforward.data.UniteId;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.data.persister.support.AbstractPersistent;
import cn.weforward.framework.support.Global;
import weforward.Bug;
import weforward.Di.DemandDi;
import weforward.Exception.StatusException;

import javax.annotation.Resource;
import java.util.Date;

public class BugImpl extends AbstractPersistent<DemandDi> implements Bug {

    @Resource
    protected String demandId;

    @Resource
    protected String description;

    @Resource
    protected String priority;

    @Resource
    protected int status;

    @Resource
    protected boolean isDealed;

    @Resource
    protected String tester;

    @Resource
    protected String dealer;

    @Resource
    protected String creator;

    @Resource
    protected String version;

    @Resource
    protected Date lastTime;


    protected BugImpl(DemandDi di) {
        super(di);
    }


    public BugImpl(DemandDi di, String user, String demandId, String description, String priority, String dealer, String version) {
        super(di);
        genPersistenceId("bug");
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
        getBusinessDi().writeLog(getId(), user, "创建了一个新的BUG", "", "");
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
    public void setStatus(int status) {
        this.status = status;
        markPersistenceUpdate();
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
    public void toBeCorrected() throws StatusException {

        if (STATUS.get(this.status).id == STATUS_ToBeCorrected.id) {
            return;
        }

        if (this.status != STATUS_ToBeRested.id && this.status != STATUS_SUGGEST.id && this.status != STATUS_APPLY.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_ToBeCorrected.getName());
        }
        this.status = STATUS_ToBeCorrected.id;
        getBusinessDi().writeLog(getId(), getUser(),"状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_ToBeCorrected.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        markPersistenceUpdate();
    }

    @Override
    public void toBeRested() throws StatusException {

        if (STATUS.get(this.status).id == STATUS_ToBeRested.id) {
            return;
        }

        if (this.status != STATUS_ToBeCorrected.id && this.status != STATUS_REOPENED.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_ToBeRested.getName());
        }
        this.status = STATUS_ToBeRested.id;
        getBusinessDi().writeLog(getId(), getUser(),"状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_ToBeRested.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        markPersistenceUpdate();
    }

    @Override
    public void toSuggest() throws StatusException {

        if (STATUS.get(this.status).id == STATUS_SUGGEST.id) {
            return;
        }

        if (this.status != STATUS_ToBeCorrected.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_SUGGEST.getName());
        }
        this.status = STATUS_SUGGEST.id;
        getBusinessDi().writeLog(getId(), getUser(),"状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_SUGGEST.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        markPersistenceUpdate();
    }

    @Override
    public void toApply() throws StatusException {

        if (STATUS.get(this.status).id == STATUS_APPLY.id) {
            return;
        }

        if (this.status != STATUS_ToBeCorrected.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_APPLY.getName());
        }
        this.status = STATUS_APPLY.id;
        getBusinessDi().writeLog(getId(), getUser(),"状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_APPLY.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        markPersistenceUpdate();
    }

    @Override
    public void toDone() throws StatusException {

        if (STATUS.get(this.status).id == STATUS_DONE.id) {
            return;
        }

        if (this.status != STATUS_ToBeRested.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_DONE.getName());
        }
        this.status = STATUS_DONE.id;
        getBusinessDi().writeLog(getId(), getUser(),"状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_DONE.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        this.isDealed = true;
        markPersistenceUpdate();
    }

    @Override
    public void toNone() throws StatusException {

        if (STATUS.get(this.status).id == STATUS_NONE.id) {
            return;
        }

        if (this.status != STATUS_SUGGEST.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_NONE.getName());
        }
        this.status = STATUS_DONE.id;
        getBusinessDi().writeLog(getId(), getUser(),"状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_NONE.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        this.isDealed = true;
        markPersistenceUpdate();
    }

    @Override
    public void toCant() throws StatusException {

        if (STATUS.get(this.status).id == STATUS_CANT.id) {
            return;
        }

        if (this.status != STATUS_APPLY.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_CANT.getName());
        }
        this.status = STATUS_CANT.id;
        getBusinessDi().writeLog(getId(), getUser(),"状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_CANT.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        this.isDealed = true;
        markPersistenceUpdate();
    }

    @Override
    public void toReopened() throws StatusException {

        if (STATUS.get(this.status).id == STATUS_REOPENED.id) {
            return;
        }

        if (this.status != STATUS_DONE.id && this.status != STATUS_NONE.id && this.status != STATUS_CANT.id) {
            throw new StatusException(STATUS.get(this.status).getName() + "不能扭转为" + STATUS_REOPENED.getName());
        }
        this.status = STATUS_REOPENED.id;
        getBusinessDi().writeLog(getId(), getUser(),"状态扭转", "状态从"+STATUS.get(this.status).getName()+"扭转为"+STATUS_REOPENED.getName(), "");
        this.lastTime = new Date(System.currentTimeMillis());
        this.isDealed = false;
        markPersistenceUpdate();
    }


    private String getUser() {
        String user = Global.TLS.getValue("user");
        if (null == user) {
            user = "admin";
        }
        return user;
    }

    @Override
    public ResultPage<BusinessLog> getLogs() {
        return getBusinessDi().getLogs(getId());
    }
}
