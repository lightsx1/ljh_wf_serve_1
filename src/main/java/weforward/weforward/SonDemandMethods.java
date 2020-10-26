package weforward.weforward;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.TransResultPage;
import cn.weforward.framework.*;
import cn.weforward.framework.doc.DocMethods;
import cn.weforward.framework.util.ValidateUtil;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocMethod;
import cn.weforward.protocol.doc.annotation.DocParameter;
import cn.weforward.protocol.ops.User;
import cn.weforward.protocol.support.datatype.FriendlyObject;
import weforward.Demand;
import weforward.exception.DemandException;
import weforward.exception.TagException;
import weforward.impl.DemandImpl;
import weforward.DemandService;
import weforward.view.SonDemandParams;
import weforward.view.SonDemandView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@DocMethods(index = 300)
@WeforwardMethods
public class SonDemandMethods implements ExceptionHandler {


    @Resource
    protected DemandService demandService;

    @WeforwardMethod
    @DocMethod(description = "创建子任务", index =0 )
    public SonDemandView createSonDemand(SonDemandParams params) throws ApiException, DemandException, TagException {

        String fid = params.getFid();
        String title = params.getTitle();
        String description = params.getDescription();
        int priority = params.getPriority();
        String charger = params.getCharger();
        Date willingStartTime = params.getWillingStartTime();
        Date willingEndTime = params.getWillingEndTime();
        String tagId = params.getTagId();
        Set<String> dealer = new HashSet<>(params.getDealer());

        ValidateUtil.isEmpty(fid, "任务父id不能为空");
        ValidateUtil.isEmpty(title, "任务标题不能为空");
        ValidateUtil.isEmpty(description, "任务详情不能为空");
        ValidateUtil.isEmpty(charger, "任务负责人不能为空");
        ValidateUtil.isEmpty(willingStartTime, "任务预计开始时间不能为空");
        ValidateUtil.isEmpty(willingEndTime, "任务预计结束时间不能为空");

        if(tagId == ""){
            tagId = null;
        }

        Demand demand = demandService.createSonDemand(getUser(),fid,title,description,priority,charger,dealer,willingStartTime,willingEndTime, tagId);
        return SonDemandView.valueOf(demand);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "demandId", type = String.class, necessary = true, description = "父任务id"))
    @DocMethod(description = "查询一个父任务下的所有子任务", index = 1 )
    public ResultPage<SonDemandView> searchSonDemand(FriendlyObject params) throws ApiException, DemandException {
        ResultPage<Demand> rp = demandService.searchSonDemand(params.getString("demandId"));
        return new TransResultPage<SonDemandView, Demand>(rp) {
            @Override
            protected SonDemandView trans(Demand src) {
                return SonDemandView.valueOf(src);
            }
        };
    }

    private String getUser() {
        User user = WeforwardSession.TLS.getOperator();
        String name = null == user ? "admin" : user.getName();
        return name;
    }

    @Override
    public Throwable exception(Throwable error) {
        if (error instanceof Exception) {
            return new ApiException(DemandServiceCode.getCode((Exception) error), error.getMessage());
        }
        return error;
    }

}
