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

        if(dealer.size() == 0){
            throw new ApiException(ApiException.CODE_INTERNAL_ERROR,"任务处理人不能为空");
        }

        if(priority != 1 && priority !=2 && priority !=3 && priority != 4){
            throw new ApiException(ApiException.CODE_INTERNAL_ERROR,"优先级只能为1、2、3 或者 4");
        }

        Demand demand = demandService.createDemand(getUser(), title, description, priority, charger, dealer, willingStartTime, willingEndTime, tagId);
        return DemandView.valueOf(demand);
    }


    @KeepServiceOrigin
    @WeforwardMethod
    @DocMethod(description = "根据关键字搜索任务", index = 1)
    public ResultPage<DemandSearchView> searchDemandsByKeywords(DemandSearchParams params) throws ApiException, DemandException {

        String keywords = params.getKeywords();
        String creator = params.getCreator();
        String charger= params.getCharger();
        String dealer = params.getDealer();
        String follower = params.getFollower();

        if(keywords == null || keywords==""){
            keywords= "";
        }

        int option = params.getOption();

        ResultPage<Demand> rp = demandService.searchDemands(keywords, creator, follower, dealer, charger,  option);

        return new TransResultPage<DemandSearchView, Demand>(rp) {
            @Override
            protected DemandSearchView trans(Demand src) {
                return DemandSearchView.valueOf(src);
            }
        };

    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "根据id获取单个任务,可获取子任务", index = 2)
    public DemandView getDemandByDemandId(FriendlyObject params) throws ApiException, DemandException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "任务id不能为空");
        Demand demand = demandService.getDemand(id);
        if (demand == null) {
            return null;
        }
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "删除任务,可删除子任务", index = 3)
    public DemandView deleteDemand(FriendlyObject params) throws ApiException, DemandException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "任务id不能为空");
        return DemandView.valueOf(demandService.deleteDemand(id));
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocMethod(description = "修改任务信息，可修改子任务", index = 4)
    public DemandView updateDemand(DemandUpdateParams params) throws ApiException, DemandException {

        String id = params.getId();
        ValidateUtil.isEmpty(id, "任务id不能为空");
        Demand demand = demandService.getDemand(id);

        String name = params.getTitle();
        if (!StringUtil.isEmpty(name)) {
            demand.setTitle(name);
        }

        int priority = params.getPriority();
        if (priority == Demand.PRIORITY_HIGHEST.id || priority == Demand.PRIORITY_HIGH.id || priority == Demand.PRIORITY_MID.id || priority == Demand.PRIORITY_LOW.id) {
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


        if(params.getDealer() !=null){
            if (params.getDealer().size() > 0) {
                Set<String> dealer = new HashSet<>(params.getDealer());
                demand.setDealer(dealer);
            }
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
    public void addTagForDemand(FriendlyObject params) throws ApiException, DemandException, TagException {
        String demandId = params.getString("demandId");
        String tagId = params.getString("tagId");
        ValidateUtil.isEmpty(demandId, "任务id不能为空");
        ValidateUtil.isEmpty(tagId, "标签id不能为空");
        demandService.addTagForDemandByTagId(demandId, tagId);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "移除任务的标签", index = 6)
    public void dropTagForDemand(FriendlyObject params) throws ApiException, TagException, DemandException {
        String demandId = params.getString("demandId");
        ValidateUtil.isEmpty(demandId, "任务id不能为空");
        demandService.dropTagForDemandByTagId(demandId);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "跟进任务", index = 7)
    public void followDemand(FriendlyObject params) throws ApiException, DemandException {
        String demandId = params.getString("demandId");
        ValidateUtil.isEmpty(demandId, "任务id不能为空");
        demandService.followDemand(params.getString("demandId"), getUser());
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "tagId", type = String.class, necessary = true, description = "标签id"))
    @DocMethod(description = "获得标签下所有任务", index = 8)
    public ResultPage<SonDemandView> getDemandsByTagId(FriendlyObject params) throws ApiException, TagException {
        String id = params.getString("tagId");
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
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "缺陷分析", index = 9)
    public DemandAnalysisView analysisBug(FriendlyObject params) throws ApiException, DemandException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "任务id不能为空");
        return demandService.analysis(id);
    }

    @WeforwardMethod
    @DocMethod(description = "获取任务日志", index = 10)
    public ResultPage<LogView> getDemandLogs(LogsParams params) throws ApiException, DemandException {
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
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为评估中", index = 11)
    public DemandView statusTurnToEvaluating(FriendlyObject params) throws ApiException, DemandException, StatusException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        demand.toEvaluating();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为规划中", index = 12)
    public DemandView statusTurnToPlanning(FriendlyObject params) throws ApiException, DemandException, StatusException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        demand.toPlanning();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为待开发", index = 13)
    public DemandView statusTurnToBeDeveloped(FriendlyObject params) throws ApiException, StatusException, DemandException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        demand.toBeDeveloped();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为开发中", index = 14)
    public DemandView statusTurnToDevloping(FriendlyObject params) throws ApiException, StatusException, DemandException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        demand.toDevloping();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为待测试", index = 15)
    public DemandView statusTurnToBeTested(FriendlyObject params) throws ApiException, StatusException, DemandException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        demand.toBeTested();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为测试中", index = 16)
    public DemandView statusTurnToTesting(FriendlyObject params) throws ApiException, StatusException, DemandException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        demand.toTesting();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为测试通过", index = 17)
    public DemandView statusTurnToTested(FriendlyObject params) throws ApiException, StatusException, DemandException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        demand.toTested();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为已上线", index = 18)
    public DemandView statusTurnToOnline(FriendlyObject params) throws ApiException, StatusException, DemandException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        demand.toOnline();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为已拒绝", index = 19)
    public DemandView statusTurnToRejected(FriendlyObject params) throws ApiException, StatusException, DemandException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        demand.toRejected();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "状态扭转为已拒绝", index = 20)
    public DemandView statusTurnToHanged(FriendlyObject params) throws ApiException, StatusException, DemandException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        demand.toHanged();
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "将任务的预期开始时间清空", index = 21)
    public DemandView clearWillingStartTime(FriendlyObject params) throws ApiException, StatusException, DemandException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        demand.setWillingStartTime(null);
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "将任务的预期结束时间清空", index = 22)
    public DemandView clearWillingEndTime(FriendlyObject params) throws ApiException, StatusException, DemandException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        demand.setWillingEndTime(null);
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "任务id"))
    @DocMethod(description = "将任务的预期结束时间清空", index = 23)
    public DemandView disFollow(FriendlyObject params) throws ApiException, StatusException, DemandException {
        String id = params.getString("demandId");
        ValidateUtil.isEmpty(id, "id不能为空");
        Demand demand = demandService.getDemand(id);
        demand.disFollow(getUser());
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
