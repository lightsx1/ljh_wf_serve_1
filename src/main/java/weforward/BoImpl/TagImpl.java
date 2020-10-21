package weforward.BoImpl;

import cn.weforward.common.NameItem;
import cn.weforward.data.UniteId;
import cn.weforward.data.persister.support.AbstractPersistent;
import weforward.Di.DemandDi;
import weforward.Bo.Tag;
import weforward.Exception.StatusException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TagImpl extends AbstractPersistent<DemandDi> implements Tag{

    /**
     * 对应需求id
     */

    @Resource
    protected String name;

    /*
    * 标签状态，1表示正常使用，2表示删除
    * */
    @Resource
    protected int status;

    protected TagImpl(DemandDi di) {super(di);
    }

    public TagImpl(DemandDi di ,String name) {
        super(di);
        genPersistenceId("tag");
        this.name = name;
        this.status = 1;
        markPersistenceUpdate();
        getBusinessDi().writeLog(getId(), "创建了一个新的标签", "", "");
    }

    @Override
    public UniteId getId() {
        return getPersistenceId();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        markPersistenceUpdate();
    }

    @Override
    public NameItem getStatus() {
        return STATUS.get(this.status);
    }


    @Override
    public void deleteTag() throws StatusException {
        this.status = 2;
    }

}
