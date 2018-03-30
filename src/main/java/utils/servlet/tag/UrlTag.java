package utils.servlet.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import jodd.util.net.URLCoder;
import jodd.util.net.URLCoder.Builder;

public class UrlTag extends SimpleTagSupport implements DynamicAttributes {
	protected String baseUrl;
	protected String var;
	private final List<String> attrs = new ArrayList();

	public void set_(String value) {
		this.baseUrl = value;
	}

	public void set_var(String value) {
		this.var = value;
	}

	public void setDynamicAttribute(String uri, String localName, Object value) {
		this.attrs.add(localName);
		this.attrs.add(value == null ? "" : value.toString());
	}

	public void doTag() throws JspException {
		PageContext pageContext = (PageContext) this.getJspContext();
		Builder builder = URLCoder.build(this.baseUrl);

		for (int out = 0; out < this.attrs.size(); out += 2) {
			builder.queryParam((String) this.attrs.get(out), (String) this.attrs.get(out + 1));
		}

		if (this.var == null) {
			JspWriter out1 = pageContext.getOut();

			try {
				out1.print(builder.toString());
			} catch (IOException arg4) {
				;
			}
		} else {
			pageContext.setAttribute(this.var, builder.toString());
		}

	}
}