package utils.servlet.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import jodd.servlet.tag.TagUtil;
import jodd.typeconverter.Converter;
import jodd.typeconverter.TypeConversionException;

public class IfTag extends SimpleTagSupport {
	private String test;

	public void setTest(String test) {
		this.test = test;
	}

	public void doTag() throws JspException {
		boolean testValue;
		try {
			testValue = Converter.get().toBooleanValue(this.test, false);
		} catch (TypeConversionException arg2) {
			testValue = false;
		}

		if (testValue) {
			TagUtil.invokeBody(this.getJspBody());
		}

	}
}