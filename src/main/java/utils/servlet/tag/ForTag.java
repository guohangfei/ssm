package utils.servlet.tag;

import javax.servlet.jsp.JspException;
import utils.servlet.tag.LoopingTagSupport;

public class ForTag extends LoopingTagSupport {
	public void doTag() throws JspException {
		this.prepareStepDirection();
		this.loopBody();
	}
}