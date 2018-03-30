package utils.servlet.tag;

import javax.servlet.jsp.JspException;
import utils.servlet.tag.LoopingTagSupport;

public class LoopTag extends LoopingTagSupport {
	protected boolean isEndSpecified;
	protected boolean isExclusive;
	protected boolean isCount;
	protected boolean autoDirection;

	public void setEnd(int end) {
		super.setEnd(end);
		if (this.isEndSpecified) {
			throw new IllegalArgumentException("End boundary already specified");
		} else {
			this.isEndSpecified = true;
		}
	}

	public void setCount(int count) {
		this.setEnd(count);
		this.isCount = true;
	}

	public void setTo(int to) {
		this.setEnd(to);
		this.isExclusive = true;
	}

	public void setAutoDirection(boolean autoDirection) {
		this.autoDirection = autoDirection;
	}

	public void doTag() throws JspException {
		if (!this.isEndSpecified) {
			throw new IllegalArgumentException("End boundary of the loop is not specified");
		} else {
			this.prepareStepDirection();
			if (this.isCount) {
				if (this.end < 0) {
					throw new IllegalArgumentException("Negative count value");
				}

				this.end = this.start + this.step * (this.end - 1);
			}

			this.prepareStepDirection(this.autoDirection, false);
			if (this.isExclusive) {
				if (this.step > 0) {
					--this.end;
				} else if (this.step < 0) {
					++this.end;
				}
			}

			this.loopBody();
		}
	}
}