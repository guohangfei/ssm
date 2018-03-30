package utils.servlet.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import jodd.servlet.tag.TagUtil;

public class SetTag extends SimpleTagSupport {
	protected String name;
	protected String scope;
	protected Object value;

	public void setName(String name) {
		this.name = name;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void doTag() throws JspException {
		PageContext pageContext = (PageContext) this.getJspContext();
		TagUtil.setScopeAttribute(this.name, this.value, this.scope, pageContext);
	}
}