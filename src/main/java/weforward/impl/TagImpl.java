package weforward.impl;

import cn.weforward.common.NameItem;
import cn.weforward.data.UniteId;
import cn.weforward.data.persister.support.AbstractPersistent;
import weforward.di.DemandDi;
import weforward.Tag;

import javax.annotation.Resource;

public class TagImpl extends AbstractPersistent<DemandDi> implements Tag{

    /** 标签名称*/
    @Resource
    protected String name;

    /** 标签状态，1表示正常使用，2表示删除*/
    @Resource
    protected int status;


    protected TagImpl(DemandDi di) {
        super(di);
    }

    public TagImpl(DemandDi di ,String name) {
        super(di);
        genPersistenceId("tag");
        this.name = name;
        this.status = 1;
        markPersistenceUpdate();
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
    public void deleteTag() {
        this.status = STATUS_DELETED.id;
        markPersistenceUpdate();
    }

}
