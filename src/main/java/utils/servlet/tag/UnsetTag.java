package utils.servlet.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class UnsetTag extends SimpleTagSupport {
	protected String name;
	private static final String SCOPE_APPLICATION = "application";
	private static final String SCOPE_SESSION = "session";
	private static final String SCOPE_REQUEST = "request";
	private static final String SCOPE_PAGE = "page";
	protected String scope;

	public void setName(String name) {
		this.name = name;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void doTag() throws JspException {
		PageContext pageContext = (PageContext) this.getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String scopeValue = this.scope != null ? this.scope.toLowerCase() : "page";
		if (scopeValue.equals("application")) {
			request.getSession().getServletContext().removeAttribute(this.name);
		} else if (scopeValue.equals("session")) {
			request.getSession().removeAttribute(this.name);
		} else if (scopeValue.equals("request")) {
			request.removeAttribute(this.name);
		} else {
			if (!scopeValue.equals("page")) {
				throw new JspException("Invalid scope: " + this.scope);
			}

			pageContext.removeAttribute(this.name);
		}

	}
}