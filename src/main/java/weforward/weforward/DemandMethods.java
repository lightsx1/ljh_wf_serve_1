package weforward.weforward;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TransResultPage;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.framework.*;
import cn.weforward.framework.doc.DocMethods;
import cn.weforward.framework.exception.ForwardException;
import cn.weforward.framework.support.Global;
import cn.weforward.framework.util.ValidateUtil;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocMethod;
import cn.weforward.protocol.doc.annotation.DocParameter;
import cn.weforward.protocol.support.datatype.FriendlyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weforward.Demand;
import weforward.View.*;
import weforward.Exception.StatusException;
import weforward.DemandService;

import javax.annotation.Resource;
import java.util.*;

@DocMethods(index = 100)
@WeforwardMethods
public class DemandMethods implements ExceptionHandler {
    final static Logger _Logger = LoggerFactory.getLogger(DemandMethods.class);

    @Resource
    protected DemandService demandService;

    //session.getAttribute()
    private String getUser() {
        String user = Global.TLS.getValue("user");
        if (null == user) {
            user = "admin";
        }
        return user;
    }


    @WeforwardMethod
    @DocMethod(description = "创建任务", index = 0)
    public DemandView createDemand(DemandParams params) throws ApiException {

        String title = params.getTitle();
        String description = params.getDescription();
        int priority = params.getPriority();
        Set<String> charger = new HashSet<>(params.getCharger());
        Date start = params.getStart();
        Date end = params.getEnd();
        //*****************************************************
        ValidateUtil.isEmpty(title, "产品标题不能为空");
        ValidateUtil.isEmpty(description, "产品详情不能为空");
        ValidateUtil.isEmpty(priority, "产品优先级不能为空");
        ValidateUtil.isEmpty(charger, "产品负责人不能为空");
        ValidateUtil.isEmpty(start, "产品预计开始时间不能为空");
        ValidateUtil.isEmpty(end, "产品预计结束时间不能为空");
        //*******************************************************
        Demand demand = demandService.createDemand(getUser(), title, description, priority, charger, start, end);
        return DemandView.valueOf(demand);
    }


    @KeepServiceOrigin
    @WeforwardMethod
    @DocMethod(description = "搜索任务", index = 1)
    public ResultPage<DemandView> searchDemands(DemandSearchParams params) throws ApiException {

        String keywords = params.getKeywords();

        int option = params.getOption();

        ResultPage<Demand> rp = demandService.searchDemands(keywords, option);

        return new TransResultPage<DemandView, Demand>(rp) {
            @Override
            protected DemandView trans(Demand src) {
                return DemandView.valueOf(src);
            }
        };
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "根据id获取单个任务", index = 2)
    public DemandView getDemand(FriendlyObject params) throws ApiException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "任务id不能为空");
        Demand demand = demandService.getDemand(id);
        if( demand == null){
            return null;
        }
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "删除任务", index = 3)
    public DemandView deleteDemand(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "任务id不能为空");
        return DemandView.valueOf(demandService.deleteDemand(id));
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocMethod(description = "更新任务", index = 4)
    public DemandView updateDemand(DemandUpdateParams params) throws ApiException, StatusException {

        String id =params.getId();
        ValidateUtil.isEmpty(id, "任务id不能为空");
        Demand demand = demandService.getDemand(id);

        if (null == demand) {
            throw new StatusException("更新失败，不存在该id的任务");
        }

        String name = params.getTitle();
        if (!StringUtil.isEmpty(name)) {
            demand.setTitle(name);
        }

        int priority = params.getPriority();
        if (priority == 1 || priority == 2 || priority == 3 || priority == 4) {
            demand.setPriority(priority);
        }

        String description = params.getDescription();
        if (!StringUtil.isEmpty(description)) {
            demand.setDescription(description);
        }

        Set<String> charger = new HashSet<>(params.getCharger());
        if (charger.size() > 0) {
            demand.setCharger(charger);
        }

        Date start = params.getStart();
        if (start != null) {
            demand.setStart(start);
        }

        Date end = params.getEnd();
        if (end != null) {
            demand.setEnd(end);
        }

        return DemandView.valueOf(demand);
    }


    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter({@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"), @DocAttribute(name = "tagId", type = String.class, necessary = true, description = "标签id")})
    @DocMethod(description = "为任务添加标签", index =5)
    public String addTagForDemand(FriendlyObject params) throws ApiException, StatusException {
        String demandId =params.getString("demandId");
        String tagId =params.getString("tagId");
        ValidateUtil.isEmpty(demandId,"任务id不能为空");
        ValidateUtil.isEmpty(tagId,"标签id不能为空");
        return demandService.addTagForDemandByTagId(demandId,tagId);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "删除任务中标签", index = 6)
    public String dropTagForDemand(FriendlyObject params) throws ApiException, StatusException {
        String demandId =params.getString("demandId");
        ValidateUtil.isEmpty(demandId, "任务id不能为空");
        return demandService.dropTagForDemandByTagId(demandId);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "跟进", index = 7)
    public String followDemand(FriendlyObject params) throws ApiException, StatusException {
        String demandId =params.getString("demandId");
        ValidateUtil.isEmpty(demandId, "任务id不能为空");
        return demandService.follow(params.getString("demandId"),getUser());
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "标签id"))
    @DocMethod(description = "获得标签下所有任务", index = 8)
    public ResultPage<SonDemandView> getDemandsByTagId(FriendlyObject params) throws ApiException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "标签id不能为空");
        ResultPage<Demand> rp = demandService .searchDemandByTagId(id);
        return new TransResultPage<SonDemandView, Demand>(rp) {
            @Override
            protected SonDemandView trans(Demand src) {
                return SonDemandView.valueOf(src);
            }
        };
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "缺陷分析", index = 9)
    public List <Map<String,Integer>> analysisBug(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "任务id");
        List< Map<String,Integer> > list = demandService.analysis(params.getString("id"));
        return list;
    }

    @WeforwardMethod
    @DocMethod(description = "获取任务日志", index = 10)
    public ResultPage<LogView> getDemandLogs(LogsParams params) throws ApiException {
        String id = params.getId();
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        ForwardException.forwardToIfNeed(demand);
        return new TransResultPage<LogView, BusinessLog>(demand.getLogs()) {
            @Override
            protected LogView trans(BusinessLog src) {
                return LogView.valueOf(src);
            }
        };
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为评估中", index = 11)
    public DemandView statusTurnToEvaluating(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if(demand == null){
            throw new StatusException("不存在该id任务");
        }
        demand.toEvaluating();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为规划中", index = 12)
    public DemandView statusTurnToPlanning(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if(demand == null){
            throw new StatusException("不存在该id任务");
        }
        demand.toPlanning();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为待开发", index = 13)
    public DemandView statusTurnToBeDeveloped(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if(demand == null){
            throw new StatusException("不存在该id任务");
        }
        demand.toBeDeveloped();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为开发中", index = 14)
    public DemandView statusTurnToDevloping(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if(demand == null){
            throw new StatusException("不存在该id任务");
        }
        demand.toDevloping();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为待测试", index = 15)
    public DemandView statusTurnToBeTested(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if(demand == null){
            throw new StatusException("不存在该id任务");
        }
        demand.toBeTested();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为测试中", index = 16)
    public DemandView statusTurnToTesting(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if(demand == null){
            throw new StatusException("不存在该id任务");
        }
        demand.toTesting();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为测试通过", index = 17)
    public DemandView statusTurnToTested(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if(demand == null){
            throw new StatusException("不存在该id任务");
        }
        demand.toTested();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为已上线", index = 18)
    public DemandView statusTurnToOnline(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if(demand == null){
            throw new StatusException("不存在该id任务");
        }
        demand.toOnline();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为已拒绝", index = 19)
    public DemandView statusTurnToRejected(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if(demand == null){
            throw new StatusException("不存在该id任务");
        }
        demand.toRejected();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为已拒绝", index = 20)
    public DemandView statusTurnToHanged(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if(demand == null){
            throw new StatusException("不存在该id任务");
        }
        demand.toHanged();
        return DemandView.valueOf(demand);
    }

    @Override
    public Throwable exception(Throwable error) {
        if (error instanceof StatusException) {
            return new ApiException(DemandServiceCode.getCode((StatusException) error), error.getMessage());
        }
        return error;
    }


}
