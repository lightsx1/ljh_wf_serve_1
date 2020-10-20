package weforward.Methods;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import cn.weforward.common.util.StringUtil;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weforward.Bo.Demand;
import weforward.Exception.StatusException;
import weforward.Params.DemandParams;
import weforward.Params.DemandSearchParams;
import weforward.Params.DemandUpdateParams;
import weforward.Service.DemandService;
import weforward.View.DemandView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@DocMethods(index = 100)
@WeforwardMethods
public class DemandMethods {
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
    @DocMethod(description = "创建需求", index =0 )
    public DemandView create(DemandParams params) throws ApiException {

        String title = params.getTitle();
        String description = params.getDescription();
        int priority = params.getPriority();
        List<String> charger = params.getCharger();
        String follower = params.getFollower();
        Date start = params.getStart();
        Date end = params.getEnd();
        //*****************************************************
        ValidateUtil.isEmpty(title, "产品标题不能为空");
        ValidateUtil.isEmpty(description, "产品详情不能为空");
        ValidateUtil.isEmpty(priority, "产品优先级不能为空");
        ValidateUtil.isEmpty(charger, "产品负责人不能为空");
        ValidateUtil.isEmpty(follower, "产品跟进人不能为空");
        ValidateUtil.isEmpty(start, "产品预计开始时间不能为空");
        ValidateUtil.isEmpty(end, "产品预计结束时间不能为空");
        //*******************************************************
        Demand demand = demandService.createDemand(getUser(),title,description,priority,charger,follower,start,end);
        return DemandView.valueOf(demand);
    }


    @KeepServiceOrigin
    @WeforwardMethod
    @DocMethod(description = "搜索需求", index = 1)
    public ResultPage<DemandView> search(DemandSearchParams params) throws ApiException {
        ResultPage<Demand> rp = demandService .searchDemands(getUser(), params.getKeywords(),params.getStatus());
        return new TransResultPage<DemandView, Demand>(rp) {
            @Override
            protected DemandView trans(Demand src) {
                return DemandView.valueOf(src);
            }
        };
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "需求id"))
    @DocMethod(description = "获取需求", index = 2)
    public DemandView get(FriendlyObject params) throws ApiException {
        return DemandView.valueOf(demandService.getDemand(params.getString("id")));
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "需求id"))
    @DocMethod(description = "删除需求", index = 3)
    public DemandView delete(FriendlyObject params) throws ApiException {
        return DemandView.valueOf(demandService.deleteDemand(params.getString("id")));
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocMethod(description = "更新产品", index = 4)
    public DemandView update(DemandUpdateParams params) throws ApiException {
        Demand demand = demandService.getDemand(params.getId());
        demand = check(demand);

        if (null == demand) {
            return null;
        }

        String name = params.getTitle();
        if (!StringUtil.isEmpty(name)) {
            demand.setTitle(name);
        }

        String follower = params.getFollower();
        if (!StringUtil.isEmpty(follower)) {
            demand.setFollower(follower);
        }

        int priority = params.getPriority();
        if( priority == 1 || priority == 2 || priority == 3|| priority == 4) {
            demand.setPriority(priority);
        }

        String description = params.getDescription();
        if (!StringUtil.isEmpty(description)) {
            demand.setDescription(description);
        }

        List<String> charger = params.getCharger();
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
    @DocParameter({@DocAttribute(name = "id", type = String.class, necessary = true, description = "需求id"),@DocAttribute(name = "status", type = Integer.class, necessary = true, description = "扭转后需求状态")})
    @DocMethod(description = "状态扭转", index = 5)
    public DemandView statusTurn(FriendlyObject params) throws ApiException {
        Demand demand = demandService.getDemand(params.getString("id"));
        demand.statusTurn(params.getInt("status"));
        return DemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter({@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "需求id"),@DocAttribute(name = "tagId", type = String.class, necessary = true, description = "标签id")})
    @DocMethod(description = "为需求添加标签", index = 6)
    public String AddTagForDemand(FriendlyObject params) throws ApiException {
        return demandService.addTagForDemand(params.getString("demandId"),params.getString("tagId"));
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "需求id"))
    @DocMethod(description = "删除需求中标签", index = 7 )
    public String DropTagForDemand(FriendlyObject params) throws ApiException, StatusException {
        return demandService.dropTagForDemand(params.getString("demandId"));
    }


    private Demand check(Demand demand) {
        if (null == demand) {
            return null;
        }
        if (!StringUtil.eq(demand.getUser(), getUser())) {
            return null;
        }
        return demand;
    }

}
