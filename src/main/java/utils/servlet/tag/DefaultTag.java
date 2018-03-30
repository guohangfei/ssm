package utils.servlet.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import utils.servlet.tag.SwitchTag;
import utils.servlet.tag.TagUtil;

public class DefaultTag extends SimpleTagSupport {
	public void doTag() throws JspException {
		JspTag parent = this.getParent();
		if (!(parent instanceof SwitchTag)) {
			throw new JspException("Parent switch tag is required.", (Throwable) null);
		} else {
			SwitchTag switchTag = (SwitchTag) parent;
			if (!switchTag.isValueFounded()) {
				TagUtil.invokeBody(this.getJspBody());
			}

		}
	}
}