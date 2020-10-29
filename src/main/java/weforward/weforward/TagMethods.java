package weforward.weforward;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.TransResultPage;
import cn.weforward.framework.*;
import cn.weforward.framework.doc.DocMethods;
import cn.weforward.framework.util.ValidateUtil;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocMethod;
import cn.weforward.protocol.doc.annotation.DocParameter;
import cn.weforward.protocol.support.datatype.FriendlyObject;

import weforward.exception.TagException;
import weforward.view.TagView;
import weforward.DemandService;
import weforward.Tag;

import javax.annotation.Resource;

@DocMethods(index = 200)
@WeforwardMethods
public class TagMethods implements ExceptionHandler {

    @Resource
    protected DemandService demandService;


    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "tagName", type = String.class, necessary = true, description = "标签名称"))
    @DocMethod(description = "创建标签", index = 0)
    public TagView createTag(FriendlyObject params) throws ApiException {

        String name = params.getString("tagName");

        ValidateUtil.isEmpty(name, "产品标题不能为空");

        Tag tag = demandService.createTag(name);
        return TagView.valueOf(tag);
    }


    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "tagName", type = String.class, necessary = true, description = "标签名称"))
    @DocMethod(description = "根据标签名称模糊搜索标签", index = 1)
    public ResultPage<TagView> searchTagByName(FriendlyObject params) {
        ResultPage<Tag> rp = demandService.searchTagByKeywords(params.getString("tagName"));
        return new TransResultPage<TagView, Tag>(rp) {
            @Override
            protected TagView trans(Tag src) {
                return TagView.valueOf(src);
            }
        };
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "tagId", type = String.class, necessary = true, description = "标签id"))
    @DocMethod(description = "删除标签", index = 2)
    public String deleteTag(FriendlyObject params) throws TagException {
        demandService.deleteTag(params.getString("tagId"));
        return "删除成功";
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter({@DocAttribute(name = "tagId", type = String.class, necessary = true, description = "标签id"), @DocAttribute(name = "tagName", type = String.class, necessary = true, description = "修改后标签名称")})
    @DocMethod(description = "更改标签", index = 3)
    public TagView updateTag(FriendlyObject params) throws TagException, ApiException {

        String id = params.getString("tagId");
        String name = params.getString("tagName");

        ValidateUtil.isEmpty(id, "标签id不能为空");
        ValidateUtil.isEmpty(name, "修改后标签名称不能为空");

        Tag tag = demandService.getTag(id);
        tag.setName(name);

        return TagView.valueOf(tag);
    }

    @Override
    public Throwable exception(Throwable error) {
        if (error instanceof Exception) {
            return new ApiException(DemandServiceCode.getCode((Exception) error), error.getMessage());
        }
        return error;
    }

}
