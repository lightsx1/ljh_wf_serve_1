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
import weforward.Bo.Demand;
import weforward.BoImpl.DemandImpl;
import weforward.Params.SonDemandParams;
import weforward.Service.DemandService;
import weforward.View.DemandView;
import weforward.View.SonDemandView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@DocMethods(index = 300)
@WeforwardMethods
public class SonDemandMethods {


    @Resource
    protected DemandService demandService;

    @WeforwardMethod
    @DocMethod(description = "创建子需求", index =0 )
    public SonDemandView create(SonDemandParams params) throws ApiException {

        String fid = params.getFid();
        String title = params.getTitle();
        String description = params.getDescription();
        int priority = params.getPriority();
        List<String> charger = params.getCharger();
        Date start = params.getStart();
        Date end = params.getEnd();
        //*****************************************************
        ValidateUtil.isEmpty(fid, "父id不能为空");
        ValidateUtil.isEmpty(title, "产品标题不能为空");
        ValidateUtil.isEmpty(description, "产品详情不能为空");
        ValidateUtil.isEmpty(priority, "产品优先级不能为空");
        ValidateUtil.isEmpty(charger, "产品负责人不能为空");
        ValidateUtil.isEmpty(start, "产品预计开始时间不能为空");
        ValidateUtil.isEmpty(end, "产品预计结束时间不能为空");
        //*******************************************************
        Demand demand = demandService.createDemand(getUser(),fid,title,description,priority,charger,start,end);
        return SonDemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "父需求id"))
    @DocMethod(description = "查询一个父需求下的所有子需求", index = 1 )
    public ResultPage<SonDemandView> search(FriendlyObject params) throws ApiException {
        ResultPage<DemandImpl> rp = demandService.SearchSonDemand(params.getString("demandId"));
        return new TransResultPage<SonDemandView, DemandImpl>(rp) {
            @Override
            protected SonDemandView trans(DemandImpl src) {
                return SonDemandView.valueOf(src);
            }
        };
    }

    //session.getAttribute()
    private String getUser() {
        String user = Global.TLS.getValue("user");
        if (null == user) {
            user = "admin";
        }
        return user;
    }
}
