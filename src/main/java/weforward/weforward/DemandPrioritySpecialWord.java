package weforward.weforward;

import cn.weforward.framework.doc.AbstractDocSpecialWord;
import org.springframework.stereotype.Component;
import weforward.Demand;

/**
 * @Author 1
 * @create 2020/10/26 14:04
 */
@Component
public class DemandPrioritySpecialWord extends AbstractDocSpecialWord {

    public DemandPrioritySpecialWord() {
        super("任务优先级", null);
        setTabelHeader("任务优先级id","任务优先级名称");
        addTableItem(Demand.PRIORITY);
    }
}
