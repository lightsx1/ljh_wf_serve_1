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
    protected List<String> did;

    @Resource
    protected String name;

    @Resource
    protected int status;

    protected TagImpl(DemandDi di) {super(di);
    }

    public TagImpl(DemandDi di ,String name) {
        super(di);
        genPersistenceId("tag");
        did = new ArrayList<>();
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
    public List<String> getDid() {
        return did;
    }

    @Override
    public void addDemandToTag(String DemandId){
        this.did.add(DemandId);
        markPersistenceUpdate();
    }

    @Override
    public void deleteDemandFromTag(String DemandId){
        if(this.did.size() == 0 || did == null) {
            return;
        }
        Iterator<String> iterator=did.iterator();
        while(iterator.hasNext()){
            if(iterator.next().equals(DemandId)){
                iterator.remove();
            }
         }
        markPersistenceUpdate();
    }

    @Override
    public void deleteTag() throws StatusException {
        if (did == null || did.size() == 0) {
            this.status = Status_DELETE.id;
            this.did = new ArrayList<>();
            markPersistenceUpdate();
        }
        else{
            throw new StatusException("该标签下还有需求，不能删除");
        }
    }

}
