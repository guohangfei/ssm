package utils.servlet.tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import jodd.servlet.ServletUtil;

public class DebugTag extends SimpleTagSupport {
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) this.getJspContext();
		JspWriter out = this.getJspContext().getOut();
		out.println(ServletUtil.debug(pageContext));
	}
}