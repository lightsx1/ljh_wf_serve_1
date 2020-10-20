package weforward.Methods;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.TransResultPage;
import cn.weforward.framework.ApiException;
import cn.weforward.framework.KeepServiceOrigin;
import cn.weforward.framework.WeforwardMethod;
import cn.weforward.framework.WeforwardMethods;
import cn.weforward.framework.doc.DocMethods;
import cn.weforward.framework.util.ValidateUtil;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocMethod;
import cn.weforward.protocol.doc.annotation.DocParameter;
import cn.weforward.protocol.support.datatype.FriendlyObject;

import weforward.Exception.StatusException;
import weforward.Service.TagService;
import weforward.View.TagCreateView;
import weforward.Bo.Tag;
import weforward.View.TagSearchView;

import javax.annotation.Resource;

@DocMethods(index = 200)
@WeforwardMethods
public class TagMethods {

    @Resource
    protected TagService tagService;

    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "name", type = String.class, necessary = true, description = "标签名称"))
    @DocMethod(description = "创建标签", index = 0)
    public TagCreateView create(FriendlyObject params) throws ApiException {

        String name = params.getString("name");

        //*****************************************************
        ValidateUtil.isEmpty(name, "产品标题不能为空");
        //*******************************************************
        Tag tag = tagService.createTag(name);
        return TagCreateView.valueOf(tag);
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocMethod(description = "获得所有标签", index = 1)
    public ResultPage<TagSearchView> getTags() throws ApiException {
        ResultPage<Tag> rp = tagService .getTags();
        return new TransResultPage<TagSearchView, Tag>(rp) {
            @Override
            protected TagSearchView trans(Tag src) {
                return TagSearchView.valueOf(src);
            }
        };
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "name", type = String.class, necessary = true, description = "标签名称"))
    @DocMethod(description = "搜索标签", index = 2)
    public ResultPage<TagSearchView> searchTag(FriendlyObject params) throws ApiException {
        ResultPage<Tag> rp = tagService .searchTag(params.getString("name"));
        return new TransResultPage<TagSearchView, Tag>(rp) {
            @Override
            protected TagSearchView trans(Tag src) {
                return TagSearchView.valueOf(src);
            }
        };
    }

    @KeepServiceOrigin
    @WeforwardMethod
    @DocParameter(@DocAttribute(name = "id", type = String.class, necessary = true, description = "标签名称"))
    @DocMethod(description = "删除标签", index = 3)
    public String deleteTag(FriendlyObject params) throws ApiException, StatusException {
        tagService.deleteTag(params.getString("id"));
        return "删除成功";
    }

}
