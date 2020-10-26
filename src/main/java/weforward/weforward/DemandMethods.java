package weforward.weforward;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TransResultPage;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.framework.*;
import cn.weforward.framework.doc.DocMethods;
import cn.weforward.framework.exception.ForwardException;
import cn.weforward.framework.util.ValidateUtil;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocMethod;
import cn.weforward.protocol.doc.annotation.DocParameter;
import cn.weforward.protocol.ops.User;
import cn.weforward.protocol.support.datatype.FriendlyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weforward.Demand;
import weforward.exception.DemandException;
import weforward.exception.TagException;
import weforward.view.*;
import weforward.exception.StatusException;
import weforward.DemandService;

import javax.annotation.Resource;
import java.util.*;

@DocMethods(index = 100)
@WeforwardMethods
public class DemandMethods implements ExceptionHandler {
    final static Logger _Logger = LoggerFactory.getLogger(DemandMethods.class);

    @Resource
    protected DemandService demandService;

    private String getUser() {
        User user = WeforwardSession.TLS.getOperator();
        String name = null == user ? "admin" : user.getName();
        return name;
    }


    @WeforwardMethod
    @DocMethod(description = "创建任务", index = 0)
    public DemandView createDemand(DemandParams params) throws ApiException, TagException {

        String title = params.getTitle();
        String description = params.getDescription();
        int priority = params.getPriority();
        String charger = params.getCharger();
        Date willingStartTime = params.getWillingStartTime();
        Date willingEndTime = params.getWillingEndTime();
        String tagId = params.getTagId();
        Set <String> dealer = new HashSet<>(params.getDealer());

        ValidateUtil.isEmpty(title, "任务标题不能为空");
        ValidateUtil.isEmpty(description, "任务详情不能为空");
        ValidateUtil.isEmpty(charger, "任务负责人不能为空");
        ValidateUtil.isEmpty(willingStartTime, "任务预计开始时间不能为空");
        ValidateUtil.isEmpty(willingEndTime, "任务预计结束时间不能为空");
        if(tagId == ""){
            tagId = null;
        }

        if(dealer.size() == 0){
            throw new ApiException(ApiException.CODE_INTERNAL_ERROR,"任务处理人不能为空");
        }

        if(priority != 1 && priority !=2 && priority !=3 && priority != 4){
            throw new ApiException(ApiException.CODE_INTERNAL_ERROR,"优先级只能为1 2 3 或者 4");
        }

        Demand demand = demandService.createDemand(getUser(), title, description, priority, charger, dealer, willingStartTime, willingEndTime, tagId);
        return DemandView.valueOf(demand);
    }


    @KeepServiceOrigin
    @WeforwardMethod
    @DocMethod(description = "根据关键字搜索任务", index = 1)
    public ResultPage<DemandSearchView> searchDemands(DemandSearchParams params) throws ApiException, DemandException {

        String keywords = params.getKeywords();

        if(keywords == null || keywords==""){
            keywords= "";
        }

        int option = params.getOption();

        ResultPage<Demand> rp = demandService.searchDemands(keywords, option);

        return new TransResultPage<DemandSearchView, Demand>(rp) {
            @Override
            protected DemandSearchView trans(Demand src) {
                return DemandSearchView.valueOf(src);
            }
        };

    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "根据id获取单个任务,可获取子任务", index = 2)
    public DemandView getDemandByDemandId(FriendlyObject params) throws ApiException {
        String id = params.getString("id");
        ValidateUtil.isEmpty(id, "任务id不能为空");
        Demand demand = demandService.getDemand(id);
        if (demand == null) {
            return null;
        }
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "删除任务,可删除子任务", index = 3)
    public DemandView deleteDemand(FriendlyObject params) throws ApiException, DemandException {
        String id = params.getString("id");
        ValidateUtil.isEmpty(id, "任务id不能为空");
        return DemandView.valueOf(demandService.deleteDemand(id));
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocMethod(description = "更新任务", index = 4)
    public DemandView updateDemand(DemandUpdateParams params) throws ApiException, DemandException {

        String id = params.getId();
        ValidateUtil.isEmpty(id, "任务id不能为空");
        Demand demand = demandService.getDemand(id);

        if ( null == demand ) {
            throw new DemandException("更新失败，不存在该id的任务");
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

        String charger = params.getCharger();
        if (!StringUtil.isEmpty(charger)) {
            demand.setCharger(charger);
        }

        Set<String> dealer = new HashSet<>(params.getDealer());
        if (dealer.size() > 0) {
            demand.setDealer(dealer);
        }

        Date willingStartTime = params.getWillingStartTime();
        if (willingStartTime != null) {
            demand.setWillingStartTime(willingStartTime);
        }

        Date willingEndTime = params.getWillingEndTime();
        if (willingEndTime != null) {
            demand.setWillingEndTime(willingEndTime);
        }

        return DemandView.valueOf(demand);
    }


    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter({@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"), @DocAttribute(name = "tagId", type = String.class, necessary = true, description = "标签id")})
    @DocMethod(description = "为任务添加标签", index = 5)
    public String addTagForDemand(FriendlyObject params) throws ApiException, DemandException, TagException {
        String demandId = params.getString("demandId");
        String tagId = params.getString("tagId");
        ValidateUtil.isEmpty(demandId, "任务id不能为空");
        ValidateUtil.isEmpty(tagId, "标签id不能为空");
        return demandService.addTagForDemandByTagId(demandId, tagId);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "删除任务中标签", index = 6)
    public String dropTagForDemand(FriendlyObject params) throws ApiException, TagException {
        String demandId = params.getString("demandId");
        ValidateUtil.isEmpty(demandId, "任务id不能为空");
        return demandService.dropTagForDemandByTagId(demandId);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "跟进", index = 7)
    public String followDemand(FriendlyObject params) throws ApiException, DemandException {
        String demandId = params.getString("demandId");
        ValidateUtil.isEmpty(demandId, "任务id不能为空");
        return demandService.follow(params.getString("demandId"), getUser());
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "标签id"))
    @DocMethod(description = "获得标签下所有任务", index = 8)
    public ResultPage<SonDemandView> getDemandsByTagId(FriendlyObject params) throws ApiException {
        String id = params.getString("id");
        ValidateUtil.isEmpty(id, "标签id不能为空");
        ResultPage<Demand> rp = demandService.searchDemandByTagId(id);
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
    public List<Map<String, Integer>> analysisBug(FriendlyObject params) throws ApiException, DemandException {
        String id = params.getString("id");
        ValidateUtil.isEmpty(id, "任务id");
        List<Map<String, Integer>> list = demandService.analysis(params.getString("id"));
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
        String id = params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if (demand == null) {
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
        String id = params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if (demand == null) {
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
        String id = params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if (demand == null) {
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
        String id = params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if (demand == null) {
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
        String id = params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if (demand == null) {
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
        String id = params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if (demand == null) {
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
        String id = params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if (demand == null) {
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
        String id = params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if (demand == null) {
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
        String id = params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if (demand == null) {
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
        String id = params.getString("id");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        if (demand == null) {
            throw new StatusException("不存在该id任务");
        }
        demand.toHanged();
        return DemandView.valueOf(demand);
    }

    @Override
    public Throwable exception(Throwable error) {
        if (error instanceof Exception) {
            return new ApiException(DemandServiceCode.getCode((Exception) error), error.getMessage());
        }
        return error;
    }


}
