package utils.servlet.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import jodd.servlet.tag.IfElseTag;
import jodd.servlet.tag.TagUtil;

public class ThenTag extends SimpleTagSupport {
	public void doTag() throws JspException {
		JspTag parent = this.getParent();
		if (!(parent instanceof IfElseTag)) {
			throw new JspException("Parent IfElse tag is required", (Throwable) null);
		} else {
			IfElseTag ifTag = (IfElseTag) parent;
			if (ifTag.getTestValue()) {
				TagUtil.invokeBody(this.getJspBody());
			}

		}
	}
}