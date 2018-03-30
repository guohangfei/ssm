package utils.servlet.tag;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import utils.exception.UncheckedException;
import utils.io.FastCharArrayWriter;
import utils.servlet.ServletUtil;

public class TagUtil {
	public static void invokeBody(JspFragment body) throws JspException {
		if (body != null) {
			try {
				body.invoke((Writer) null);
			} catch (IOException arg1) {
				throw new JspException("Tag body failed", arg1);
			}
		}
	}

	public static void invokeBody(JspFragment body, Writer writer) throws JspException {
		if (body != null) {
			try {
				body.invoke(writer);
			} catch (IOException arg2) {
				throw new JspException("Tag body failed", arg2);
			}
		}
	}

	public static char[] renderBody(JspFragment body) throws JspException {
		FastCharArrayWriter writer = new FastCharArrayWriter();
		invokeBody(body, writer);
		return writer.toCharArray();
	}

	public static String renderBodyToString(JspFragment body) throws JspException {
		char[] result = renderBody(body);
		return new String(result);
	}

	public static void setScopeAttribute(String name, Object value, String scope, PageContext pageContext)
			throws JspException {
		try {
			ServletUtil.setScopeAttribute(name, value, scope, pageContext);
		} catch (UncheckedException arg4) {
			throw new JspException(arg4);
		}
	}

	public static void removeScopeAttribute(String name, String scope, PageContext pageContext) throws JspException {
		try {
			ServletUtil.removeScopeAttribute(name, scope, pageContext);
		} catch (UncheckedException arg3) {
			throw new JspException(arg3);
		}
	}
}