package weforward.Methods;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.TransResultPage;
import cn.weforward.framework.ApiException;
import cn.weforward.framework.KeepServiceOrigin;
import cn.weforward.framework.WeforwardMethod;
import cn.weforward.framework.WeforwardMethods;
import cn.weforward.framework.doc.DocMethods;
import cn.weforward.framework.support.Global;
import cn.weforward.framework.util.ValidateUtil;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocMethod;
import cn.weforward.protocol.doc.annotation.DocParameter;
import cn.weforward.protocol.support.datatype.FriendlyObject;
import weforward.Bo.Bug;
import weforward.Params.BugParams;
import weforward.Service.DemandService;
import weforward.View.BugView;

import javax.annotation.Resource;

@DocMethods(index = 400)
@WeforwardMethods
public class BugMethods {

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
    @DocMethod(description = "创建Bug", index = 0)
    public BugView create(BugParams params) throws ApiException {

        String demandId = params.getDemandId();
        String description = params.getDescription();
        int priority = params.getPriority();
        String dealer = params.getDealer();
        String version = params.getVersion();

        //*****************************************************
        ValidateUtil.isEmpty(demandId, "产品标题不能为空");
        ValidateUtil.isEmpty(description, "产品详情不能为空");
        ValidateUtil.isEmpty(priority, "产品优先级不能为空");
        ValidateUtil.isEmpty(dealer, "产品预计开始时间不能为空");
        ValidateUtil.isEmpty(version, "产品预计结束时间不能为空");
        //*******************************************************
        Bug bug = demandService.createBug(getUser(), demandId, description, priority, dealer,version);
        return BugView.valueOf(bug);
    }


    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "需求id"))
    @DocMethod(description = "根据需求Id搜索Bug", index = 1)
    public TransResultPage<BugView, Bug> searchBugByDemandId(FriendlyObject params) throws ApiException {
        String id = params.getString("id");
        ResultPage <Bug> rp =demandService.searchBugByDemandId(id);

        return new TransResultPage<BugView, Bug>(rp) {
            @Override
            protected BugView trans(Bug src) {
                return BugView.valueOf(src);
            }
        };
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter({@DocAttribute(name = "id", type = String.class, necessary = true, description = "Bug_id"), @DocAttribute(name = "status", type = Integer.class, necessary = true, description = "扭转后需求状态")})
    @DocMethod(description = "状态扭转", index = 2)
    public BugView statusTurn(FriendlyObject params) throws ApiException {
        Bug bug = demandService.getBug(params.getString("id"));
        bug.statusTurn(params.getInt("status"));
        return BugView.valueOf(bug);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "Bug_id"))
    @DocMethod(description = "根据id获取单个Bug", index = 3)
    public BugView get(FriendlyObject params) throws ApiException {
        Bug bug = demandService.getBug(params.getString("id"));
        return BugView.valueOf(bug);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "keyword", type = String.class, necessary = true, description = "关键字"))
    @DocMethod(description = "根据关键字获取所有Bug", index = 3)
    public BugView getAllBugs(FriendlyObject params) throws ApiException {
        Bug bug = demandService.getBug(params.getString("keyword"));
        return BugView.valueOf(bug);
    }

}
