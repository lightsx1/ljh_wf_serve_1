package weforward.BoImpl;

import cn.weforward.common.NameItem;
import cn.weforward.data.UniteId;
import cn.weforward.data.persister.support.AbstractPersistent;
import cn.weforward.framework.support.Global;
import weforward.Bo.Demand;
import weforward.Di.DemandDi;
import weforward.Exception.StatusException;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

public class DemandImpl  extends AbstractPersistent<DemandDi> implements Demand {

    @Resource
    protected String user;

    /*需求父需求Id*/
    @Resource
    protected String fid;


    /*需求标题*/
    @Resource
    protected String title;

    /*需求具体描述*/
    @Resource
    protected String description;

    /*需求优先级*/
    @Resource
    protected int priority;

    /*需求负责人*/
    @Resource
    protected List<String> charger;

    /*需求跟进人*/
    @Resource
    protected String follower;

    /*需求预期开始时间*/
    @Resource
    protected Date start;

    /*需求预期结束时间*/
    @Resource
    protected Date end;

    /*需求结束时间*/
    @Resource
    protected Date endtime;

    /*需求创建时间*/
    @Resource
    protected Date creattime;

    /*需求当前状态*/
    @Resource
    protected int status;

    /*需求创建人名称*/
    @Resource
    protected String creator;

    @Resource
    protected String tagId;

    protected DemandImpl(DemandDi di) {
        super(di);
    }

    public DemandImpl(DemandDi di,String user,String title, String description, int priority, List<String> charger,
                      Date start, Date end) {
        super(di);
        genPersistenceId(user);
        this.user = user;
        this.title = title;
        this.description = description;
        this.priority = PRIORITY.get(priority).id;
        this.charger = charger;
        this.start = start;
        this.end = end;
        this.status = STATUS_PINGGU.id;
        this.creattime = new Date(System.currentTimeMillis());
        this.creator = user;
        this.fid = null;
        markPersistenceUpdate();
        getBusinessDi().writeLog(getId(), "创建了一个新的需求", "", "");
    }

    /**
     * 带父id参数的构造函数
     * @param di
     * @param fid
     * @param title
     * @param description
     * @param priority
     * @param charger
     * @param start
     * @param end
     * @return
     */

    public DemandImpl(DemandDi di,String user,String fid, String title, String description, int priority, List<String> charger,
                      Date start, Date end) {
        super(di);
        genPersistenceId(user);
        this.fid = fid;
        this.title = title;
        this.description = description;
        this.priority = PRIORITY.get(priority).id;
        this.charger = charger;
        this.start = start;
        this.end = end;
        this.status = STATUS_PINGGU.id;
        this.creattime = new Date(System.currentTimeMillis());
        this.creator = user;
        markPersistenceUpdate();
        getBusinessDi().writeLog(getId(), "创建了一个新的子需求", "", "");
    }

    @Override
    public UniteId getId() {
        return getPersistenceId();
    }

    @Override
    public String getFid() {
        return fid;
    }

    @Override
    public void setFid(String fid) {
        this.fid = fid;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        if(this.title.equals(title)) return;
        this.title = title;
        markPersistenceUpdate();
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        if(this.description.equals(description)) return;
        this.description = description;
        markPersistenceUpdate();
    }

    @Override
    public List<String> getCharger() {
        return this.charger;
    }

    @Override
    public void setCharger(List<String> charger) {
        if(this.charger.toString().equals(charger.toString())) return;
        this.charger = charger;
        markPersistenceUpdate();
    }

    @Override
    public String getFollower() {
        return this.follower;
    }

    @Override
    public void follow() {
        String follower = Global.TLS.getValue("user");
        if (null == user) {
            user = "admin";
        }

        if(this.follower != null) {
            if(this.follower.equals(follower)) {
                return;
            }
        }
        this.follower = follower;
        markPersistenceUpdate();
    }

    @Override
    public Date getStart() {
        return this.start;
    }

    @Override
    public void setStart(Date start) {
        if(this.start.equals(start)) return;
        this.start = start;
        markPersistenceUpdate();
    }

    @Override
    public Date getEnd() {
        return this.end;
    }

    @Override
    public void setEnd(Date end) {
        if(this.end.equals(end)) return;
        this.end = end;
        markPersistenceUpdate();
    }

    @Override
    public Date getEndTime() {
        return this.endtime;
    }

    @Override
    public void setEndTime(Date endTime) {
        if(this.endtime.equals(endTime)) return;
        this.endtime = endTime;
        markPersistenceUpdate();
    }

    @Override
    public Date getCreateTime() {
        return this.creattime;
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

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public void statusTurn(int status){

        if(STATUS.get(this.status).equals(STATUS_PINGGU)){
            if(status != 2 && status != 9) return;
            this.status = status;
        }

        if(STATUS.get(this.status).equals(STATUS_GUIHUA)){
            if(status != 3 && status != 9 && status !=10) return;
            this.status = status;
        }

        if(STATUS.get(this.status).equals(STATUS_DAIKAIFA)){
            if(status != 4 && status != 9 && status !=10 ) return;
            this.status = status;
        }

        if(STATUS.get(this.status).equals(STATUS_KAIFAZHONG)){
            if(status !=5 && status != 9 && status !=10 ) return;
            this.status = status;
        }

        if(STATUS.get(this.status).equals(STATUS_DAICESHI)){
            if(status != 4 && status != 6 && status !=9 && status !=10 ) return;
            this.status = status;
        }

        if(STATUS.get(this.status).equals(STATUS_CESHIZHONG)){
            if(status != 4 && status != 7 && status !=9 && status !=10 ) return;
            this.status = status;
        }

        if(STATUS.get(this.status).equals(STATUS_CESHITONGGUO)){
            if(status != 4 && status != 8 && status !=9 && status !=10 ) return;
            this.status = status;
        }

        if(STATUS.get(this.status).equals(STATUS_YISHANGXIAN)){
            return;
        }

        if(STATUS.get(this.status).equals(STATUS_YIJUJUE)){
            return;
        }

        if(STATUS.get(this.status).equals(STATUS_GUAQI)){
            if(status != 1 && status != 3 && status != 5 && status != 7 && status != 9 ) return;
            this.status = status;
        }

        if(this.status == 7 || this.status == 8) {
            this.end = new Date(System.currentTimeMillis());
        }

        markPersistenceUpdate();
        getBusinessDi().writeLog(getId(), "状态扭转", "", "");
    }

    /**
     * 逻辑删除一个需求
     * @param
     * @return
     */

    @Override
    public void delete() {
        this.status = STATUS_SHANCHU.id;
        this.tagId = null;
        markPersistenceUpdate();
    }

    @Override
    public void addTagForDemand(String demandId , String tagId){
        getBusinessDi().AddTagForDemand(demandId,tagId);
    }


    @Override
    public void DropTagForDemand(String demandId) throws StatusException {
        getBusinessDi().DropTagForDemand(demandId);
    }


}
