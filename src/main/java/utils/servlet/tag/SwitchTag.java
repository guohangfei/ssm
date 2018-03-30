package utils.servlet.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import jodd.servlet.tag.TagUtil;

public class SwitchTag extends SimpleTagSupport {
	static final String MSG_PARENT_SWITCH_REQUIRED = "Parent switch tag is required.";
	private String value;
	private boolean valueFounded;

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void valueFounded() {
		this.valueFounded = true;
	}

	public boolean isValueFounded() {
		return this.valueFounded;
	}

	public void doTag() throws JspException {
		TagUtil.invokeBody(this.getJspBody());
	}
}