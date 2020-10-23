package weforward.weforward;

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
import weforward.Demand;
import weforward.Exception.StatusException;
import weforward.Impl.DemandImpl;
import weforward.DemandService;
import weforward.View.SonDemandParams;
import weforward.View.SonDemandView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DocMethods(index = 300)
@WeforwardMethods
public class SonDemandMethods {


    @Resource
    protected DemandService demandService;

    @WeforwardMethod
    @DocMethod(description = "创建子任务", index =0 )
    public SonDemandView createSonDemand(SonDemandParams params) throws ApiException, StatusException {

        String fid = params.getFid();
        String title = params.getTitle();
        String description = params.getDescription();
        int priority = params.getPriority();
        Set<String> charger = new HashSet<>(params.getCharger());
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
        demandService.getDemand(fid).writeSonLog(getUser());
        return SonDemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "父任务id"))
    @DocMethod(description = "查询一个父任务下的所有子任务", index = 1 )
    public ResultPage<SonDemandView> searchSonDemand(FriendlyObject params) throws ApiException {
        ResultPage<DemandImpl> rp = demandService.searchSonDemand(params.getString("demandId"));
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
