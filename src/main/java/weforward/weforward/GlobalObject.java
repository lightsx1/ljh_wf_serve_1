package weforward.weforward;

import cn.weforward.framework.doc.DocObjectProvider;
import cn.weforward.protocol.datatype.DataType;
import cn.weforward.protocol.doc.DocAttribute;
import cn.weforward.protocol.doc.DocObject;
import cn.weforward.protocol.support.doc.DocAttributeVo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class GlobalObject implements DocObjectProvider, DocObject {

    @Override
    public DocObject get() {
        return this;
    }

    @Override
    public String getName() {
        return "_global";
    }

    @Override
    public String getDescription() {
        return "全局参数（在所有请求里面带上）";
    }

    @Override
    public List<DocAttribute> getAttributes() {
        DocAttributeVo vo = new DocAttributeVo();
        vo.name = "user";
        vo.description = "当前操作用户名";
        vo.type = DataType.STRING.value;
        return Collections.singletonList(vo);
    }

}
