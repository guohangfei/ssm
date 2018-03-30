package utils.servlet.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import utils.servlet.tag.TagUtil;
import utils.util.LoopIterator;

public abstract class LoopingTagSupport extends SimpleTagSupport {
	protected int start;
	protected int end;
	protected int step = 1;
	protected String status;
	protected int modulus = 2;

	public void setStart(int start) {
		this.start = start;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setModulus(int modulus) {
		this.modulus = modulus;
	}

	protected void prepareStepDirection() {
		if (this.step == 0) {
			this.step = this.start <= this.end ? 1 : -1;
		}

	}

	protected void prepareStepDirection(boolean autoDirection, boolean checkDirection) {
		if (this.step == 0) {
			this.step = this.start <= this.end ? 1 : -1;
		} else if (autoDirection) {
			if (this.step < 0) {
				throw new IllegalArgumentException("Step value can\'t be negative: " + this.step);
			} else {
				if (this.start > this.end) {
					this.step = -this.step;
				}

			}
		} else {
			if (checkDirection) {
				if (this.start < this.end) {
					if (this.step < 0) {
						throw new IllegalArgumentException("Negative step value for increasing loop");
					}

					return;
				}

				if (this.start > this.end && this.step > 0) {
					throw new IllegalArgumentException("Positive step value for decreasing loop");
				}
			}

		}
	}

	protected void loopBody() throws JspException {
		JspFragment body = this.getJspBody();
		if (body != null) {
			LoopIterator loopIterator = new LoopIterator(this.start, this.end, this.step, this.modulus);
			if (this.status != null) {
				this.getJspContext().setAttribute(this.status, loopIterator);
			}

			while (loopIterator.next()) {
				TagUtil.invokeBody(body);
			}

			if (this.status != null) {
				this.getJspContext().removeAttribute(this.status);
			}

		}
	}
}