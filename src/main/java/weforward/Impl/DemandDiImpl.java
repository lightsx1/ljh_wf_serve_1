package weforward.Impl;

import cn.weforward.common.ResultPage;
import cn.weforward.data.UniteId;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.data.log.BusinessLogger;
import cn.weforward.data.log.BusinessLoggerFactory;
import cn.weforward.data.persister.Persistent;
import cn.weforward.data.persister.Persister;
import cn.weforward.data.persister.PersisterFactory;
import weforward.Bug;
import weforward.Demand;
import weforward.Tag;
import weforward.Di.DemandDi;
import weforward.Exception.StatusException;

public class DemandDiImpl implements DemandDi{

    /*持久化工厂*/
    protected PersisterFactory Factory;

    /*任务持久器*/
    protected Persister<DemandImpl> demandPersistent;


    protected Persister<TagImpl> tagPersister;


    protected Persister<BugImpl> bugPersister;

    /*日志记录*/
    protected BusinessLogger m_BusinessLogger;



    public DemandDiImpl(PersisterFactory factory, BusinessLoggerFactory loggerFactory) {
        Factory = factory;
        demandPersistent = Factory.createPersister(DemandImpl.class,this);
        tagPersister = Factory.createPersister(TagImpl.class,this);
        bugPersister = Factory.createPersister(BugImpl.class,this);
        m_BusinessLogger = loggerFactory.createLogger("demand_log");
    }

    /**
     * 根据id，从持久器中取得对应的任务
     * @param id
     * @return
     */
    @Override
    public Demand getDemand(UniteId id) {
        return demandPersistent.get(id);
    }

    @Override
    public Tag getTag(UniteId id) {
        return tagPersister.get(id);
    }

    @Override
    public Bug getBug(UniteId id) {
        return bugPersister.get(id);
    }

    @Override
    public void AddTagForDemand(String demandId, String tagId) {
        demandPersistent.get(demandId).setTagId(tagId);
    }

    @Override
    public void DropTagForDemand(String demandId) throws StatusException {
        Demand demand = demandPersistent.get(demandId);
        if(demand.getTagId()==null || demand.getTagId().equals("")){
            throw new StatusException("任务的标签为空，不能删除标签");
        }
        demandPersistent.get(demandId).setTagId(null);
    }


    @Override
    public void writeLog(UniteId id, String user, String action, String what, String note) {
        m_BusinessLogger.writeLog(id.getId(), user, action, what, note);
    }

    @Override
    public ResultPage<BusinessLog> getLogs(UniteId id) {
        return m_BusinessLogger.getLogs(id.getId());
    }

    @Override
    public <E extends Persistent> Persister<E> getPersister(Class<E> clazz) {
        return Factory.getPersister(clazz);
    }

}