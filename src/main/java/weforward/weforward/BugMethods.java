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
import weforward.Bug;
import weforward.exception.DemandException;
import weforward.view.*;
import weforward.exception.StatusException;
import weforward.DemandService;

import javax.annotation.Resource;


@DocMethods(index = 400)
@WeforwardMethods
public class BugMethods implements ExceptionHandler {

    @Resource
    protected DemandService demandService;

    private String getUser() {
        User user = WeforwardSession.TLS.getOperator();
        String name = null == user ? "admin" : user.getName();
        return name;
    }


    @WeforwardMethod
    @DocMethod(description = "创建缺陷", index = 0)
    public BugView createBug(BugParams params) throws ApiException, DemandException {

        String demandId = params.getDemandId();
        String description = params.getDescription();
        String priority = params.getPriority();
        String dealer = params.getDealer();
        String version = params.getVersion();

        //*****************************************************
        ValidateUtil.isEmpty(demandId, "缺陷id不能为空");
        ValidateUtil.isEmpty(description, "缺陷详情不能为空");
        ValidateUtil.isEmpty(priority, "缺陷严重性不能为空");
        ValidateUtil.isEmpty(dealer, "缺陷处理人不能为空");
        ValidateUtil.isEmpty(version, "版本与平台不能为空");
        //*******************************************************
        Bug bug = demandService.createBug(getUser(), demandId, description, priority, dealer,version);
        return BugView.valueOf(bug);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocMethod(description = "更新缺陷", index = 4)
    public BugView updateBug(BugUpdateParams params) throws ApiException {

        String bugId =params.getBugId();
        ValidateUtil.isEmpty(bugId, "缺陷id不能为空");
        Bug bug = demandService.getBug(bugId);

        if (null == bug) {
            return null;
        }

        String description = params.getDescription();
        if (!StringUtil.isEmpty(description)) {
            bug.setDescription(description);
        }

        String dealer = params.getDealer();
        if (!StringUtil.isEmpty(dealer)) {
            bug.setDealer(dealer);
        }

        String priority = params.getPriority();
        if (!StringUtil.isEmpty(priority)) {
            bug.setPriority(priority);
        }

        String tester = params.getTester();
        if (!StringUtil.isEmpty(tester)) {
            bug.setTester(tester);
        }

        return BugView.valueOf(bug);
    }


    @WeforwardMethod
    @DocParameter({@DocAttribute(name = "id", type = String.class, necessary = true, description = "任务id"),@DocAttribute(name = "keyword", type = String.class, necessary = true, description = "关键字")})
    @DocMethod(description = "根据任务Id搜索Bug", index = 1)
    public TransResultPage<BugView, Bug> searchBugsByDemandId(FriendlyObject params) throws ApiException {
        String id =params.getString("id");
        String keyword = params.getString("keyword");
        ValidateUtil.isEmpty(id, "任务id不能为空");
        ResultPage <Bug> rp =demandService.searchBugByDemandId(id,keyword);

        return new TransResultPage<BugView, Bug>(rp) {
            @Override
            protected BugView trans(Bug src) {
                return BugView.valueOf(src);
            }
        };
    }



    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "缺陷id"))
    @DocMethod(description = "根据id获取单个缺陷", index = 2)
        public BugView getBugByBugId(FriendlyObject params) throws ApiException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "缺陷id不能为空");
        Bug bug = demandService.getBug(id);
        return BugView.valueOf(bug);
    }

    
    @WeforwardMethod
    @DocMethod(description = "获取缺陷日志", index = 4)
    public ResultPage<LogView> getBugLogs(LogsParams params) throws ApiException {
        String id = params.getId();
        ValidateUtil.isEmpty(id, "id不能为空");
        Bug bug = demandService.getBug(id);
        ForwardException.forwardToIfNeed(bug);
        return new TransResultPage<LogView, BusinessLog>(bug.getLogs()) {
            @Override
            protected LogView trans(BusinessLog src) {
                return LogView.valueOf(src);
            }
        };
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "缺陷id"))
    @DocMethod(description = "状态扭转为待修正", index = 5)
    public BugView statusTurnToBeCorrected(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "缺陷id不能为空");
        Bug bug = demandService.getBug(id);
        bug.toBeCorrected();
        return BugView.valueOf(bug);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "缺陷id"))
    @DocMethod(description = "状态扭转为待复测", index = 6)
    public BugView statusTurnToBeRested(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "缺陷id不能为空");
        Bug bug = demandService.getBug(id);
        bug.toBeRested();
        return BugView.valueOf(bug);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "缺陷id"))
    @DocMethod(description = "状态扭转为建议不做修改", index = 7)
    public BugView statusTurnToSuggest(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "缺陷id不能为空");
        Bug bug = demandService.getBug(id);
        bug.toSuggest();
        return BugView.valueOf(bug);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "缺陷id"))
    @DocMethod(description = "状态扭转为申请无法解决", index = 8)
    public BugView statusTurnToApply(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "缺陷id不能为空");
        Bug bug = demandService.getBug(id);
        bug.toApply();
        return BugView.valueOf(bug);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "缺陷id"))
    @DocMethod(description = "状态扭转为已解决", index = 9)
    public BugView statusTurnToDone(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "缺陷id不能为空");
        Bug bug = demandService.getBug(id);
        bug.toDone();
        return BugView.valueOf(bug);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "缺陷id"))
    @DocMethod(description = "状态扭转为不做修改", index = 10)
    public BugView statusTurnToNone(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "缺陷id不能为空");
        Bug bug = demandService.getBug(id);
        bug.toNone();
        return BugView.valueOf(bug);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "缺陷id"))
    @DocMethod(description = "状态扭转为无法解决", index = 11)
    public BugView statusTurnToCant(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "缺陷id不能为空");
        Bug bug = demandService.getBug(id);
        bug.toCant();
        return BugView.valueOf(bug);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "缺陷id"))
    @DocMethod(description = "状态扭转为重新打开", index = 12)
    public BugView statusTurnToReopened(FriendlyObject params) throws ApiException, StatusException {
        String id =params.getString("id");
        ValidateUtil.isEmpty(id, "缺陷id不能为空");
        Bug bug = demandService.getBug(id);
        bug.toReopened();
        return BugView.valueOf(bug);
    }

    @Override
    public Throwable exception(Throwable error) {
        if (error instanceof Exception) {
            return new ApiException(DemandServiceCode.getCode((Exception) error), error.getMessage());
        }
        return error;
    }

}
