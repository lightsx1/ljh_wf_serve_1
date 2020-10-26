package weforward.impl;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.data.UniteId;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.data.log.BusinessLogger;
import cn.weforward.data.log.BusinessLoggerFactory;
import cn.weforward.data.persister.Persistent;
import cn.weforward.data.persister.Persister;
import cn.weforward.data.persister.PersisterFactory;
import cn.weforward.framework.WeforwardSession;
import cn.weforward.protocol.ops.User;
import weforward.Bug;
import weforward.Demand;
import weforward.exception.DemandException;
import weforward.exception.TagException;
import weforward.Tag;
import weforward.di.DemandDi;

import java.util.*;

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
    public void addTagForDemand(String demandId, String tagId) {
        demandPersistent.get(demandId).setTagId(tagId);
    }

    @Override
    public void dropTagForDemand(String demandId) throws TagException {
        demandPersistent.get(demandId).setTagId(null);
    }

    @Override
    public Bug createBug(DemandDi di, String user, String demandId, String description, String priority, String dealer,String version){
        return new BugImpl(di, user, demandId, description, priority, dealer, version);
    }

    @Override
    public List<Map<String, Integer>> analysis(ResultPage<? extends Bug> rp) {

        List<Map<String,Integer>> list = new ArrayList<>();
        HashMap<String,Integer> finishMap = new HashMap<>();
        HashMap <String,Integer> statusMap = new HashMap<>();
        HashMap <String,Integer> testerMap = new HashMap<>();
        HashMap <String,Integer> dealerMap = new HashMap<>();

        for (Bug bug : ResultPageHelper.toForeach(rp)) {

            if( finishMap.get("总数") ==null){
                finishMap.put("总数",1);
            }
            else{
                finishMap.put("总数",finishMap.get("总数")+1);
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
    public void writeLog(UniteId id, String action, String what, String note) {
        User user = WeforwardSession.TLS.getOperator();
        String author = null == user ? "system" : user.getName();
        m_BusinessLogger.writeLog(id.getId(), author, action, what, note);
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
