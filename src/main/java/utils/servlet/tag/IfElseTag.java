package utils.servlet.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import jodd.servlet.tag.TagUtil;
import jodd.typeconverter.Converter;
import jodd.typeconverter.TypeConversionException;

public class IfElseTag extends SimpleTagSupport {
	private boolean testValue;

	public void setTest(String test) {
		try {
			this.testValue = Converter.get().toBooleanValue(test, false);
		} catch (TypeConversionException arg2) {
			this.testValue = false;
		}

	}

	public boolean getTestValue() {
		return this.testValue;
	}

	public void doTag() throws JspException {
		TagUtil.invokeBody(this.getJspBody());
	}
}