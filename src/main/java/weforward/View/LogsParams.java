package weforward.View;

import cn.weforward.framework.doc.DocPageParams;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

/**
 * 日志搜索参数
 * 
 * @author daibo
 *
 */
@DocObject(description = "任务日志搜索参数")
public class LogsParams extends DocPageParams {

	protected String m_Id;

	public LogsParams() {

	}

	public void setId(String id) {
		m_Id = id;
	}

	@DocAttribute(description = "任务id")
	public String getId() {
		return m_Id;
	}
}
