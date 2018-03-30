package utils.servlet.tag;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import utils.servlet.CsrfShield;

public class CsrfTokenTag extends SimpleTagSupport {
	protected String name;

	public void setName(String name) {
		this.name = name;
	}

	public void doTag() throws IOException {
		JspContext jspContext = this.getJspContext();
		HttpServletRequest request = (HttpServletRequest) ((PageContext) jspContext).getRequest();
		HttpSession session = request.getSession();
		String value = CsrfShield.prepareCsrfToken(session);
		if (this.name == null) {
			this.name = "_csrf_token";
		}

		jspContext.getOut().write("<input type=\"hidden\" name=\"" + this.name + "\" value=\"" + value + "\"/>");
	}
}