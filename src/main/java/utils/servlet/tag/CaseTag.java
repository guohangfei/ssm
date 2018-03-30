package utils.servlet.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import jodd.servlet.tag.SwitchTag;
import jodd.servlet.tag.TagUtil;

public class CaseTag extends SimpleTagSupport {
	private String value;

	public void setValue(String value) {
		this.value = value;
	}

	public void doTag() throws JspException {
		JspTag parent = this.getParent();
		if (!(parent instanceof SwitchTag)) {
			throw new JspException("Parent switch tag is required.", (Throwable) null);
		} else {
			SwitchTag switchTag = (SwitchTag) parent;
			if (switchTag.getValue() != null && switchTag.getValue().equals(this.value)) {
				switchTag.valueFounded();
				TagUtil.invokeBody(this.getJspBody());
			}

		}
	}
}