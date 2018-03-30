package utils.lagarto.form;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import utils.lagarto.LagartoParser;
import utils.lagarto.form.FormFieldResolver;
import utils.lagarto.form.FormProcessorVisitor;
import utils.servlet.JspResolver;

public class FormTag extends BodyTagSupport {
	public int doStartTag() {
		return 2;
	}

	public int doAfterBody() throws JspException {
		BodyContent body = this.getBodyContent();
		JspWriter out = body.getEnclosingWriter();
		String bodytext = this.populateForm(body.getString(), (name) -> {
			return JspResolver.value(name, this.pageContext);
		});

		try {
			out.print(bodytext);
			return 0;
		} catch (IOException arg4) {
			throw new JspException(arg4);
		}
	}

	public int doEndTag() {
		return 6;
	}

	protected String populateForm(String formHtml, FormFieldResolver resolver) {
		LagartoParser lagartoParser = new LagartoParser(formHtml);
		StringBuilder result = new StringBuilder();
		lagartoParser.parse(new FormProcessorVisitor(result, resolver));
		return result.toString();
	}
}