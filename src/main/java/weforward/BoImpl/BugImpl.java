package weforward.BoImpl;

import cn.weforward.data.UniteId;
import cn.weforward.data.persister.support.AbstractPersistent;
import weforward.Bo.Bug;
import weforward.Di.DemandDi;

import javax.annotation.Resource;
import java.util.Date;

public class BugImpl extends AbstractPersistent<DemandDi> implements Bug {

    @Resource
    protected String demandId;

    @Resource
    protected String description;

    @Resource
    protected int priority;

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
    protected Date lastTime;

    protected BugImpl(DemandDi di) {
        super(di);
    }


    public BugImpl(DemandDi di,String user, String demandId, String description, int priority, int status, String dealer, Date lastTime) {
        super(di);
        genPersistenceId(user);
        this.demandId = demandId;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.isDealed = false;
        this.tester = null;
        this.dealer = dealer;
        this.creator = user;
        this.lastTime = lastTime;
        this.lastTime = new Date(System.currentTimeMillis());
        markPersistenceUpdate();
        getBusinessDi().writeLog(getId(), "创建了一个新的BUG", "", "");
    }

    @Override
    public UniteId getId() {
        return null;
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
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
        markPersistenceUpdate();
    }

    @Override
    public int getStatus() {
        return status;
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
    public void setDealed(boolean dealed) {
        isDealed = dealed;
        markPersistenceUpdate();
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
    public void setCreator(String creator) {
        this.creator = creator;
        markPersistenceUpdate();
    }

    @Override
    public Date getLastTime() {
        return lastTime;
    }

    @Override
    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
        markPersistenceUpdate();
    }

    @Override
    public void statusTurn(int status){

        if(STATUS.get(this.status).equals(STATUS_DAIXIUZHENG)){
            if(status != 2 && status != 3 && status != 4 ) return;
            this.status = status;
        }

        if(STATUS.get(this.status).equals(STATUS_DAIFUCE)){
            if(status != 1 && status != 5 ) return;
            this.status = status;
        }

        if(STATUS.get(this.status).equals(STATUS_JIANYI)){
            if(status != 1 && status != 6 ) return;
            this.status = status;
        }

        if(STATUS.get(this.status).equals(STATUS_SHENQING)){
            if(status != 1 && status != 7 ) return;
            this.status = status;
        }

        if(STATUS.get(this.status).equals(STATUS_YIJIEJUE)){
            if(status != 8 ) return;
            this.status = status;
        }

        if(STATUS.get(this.status).equals(STATUS_BUZUOXIUGAI)){
            if(status != 8 ) return;
            this.status = status;
        }

        if(STATUS.get(this.status).equals(STATUS_WUFAJIEJUE)){
            if(status != 8 ) return;
            this.status = status;
        }

        if(STATUS.get(this.status).equals(STATUS_CHONGXINDAKAI)){
            if(status != 2) return;
            this.status = status;
        }

        markPersistenceUpdate();
        getBusinessDi().writeLog(getId(), "Bug状态扭转", "", "");
    }
}
