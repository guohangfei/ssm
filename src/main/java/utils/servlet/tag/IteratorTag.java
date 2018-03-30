package utils.servlet.tag;

import java.util.Collection;
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import jodd.servlet.tag.IteratorStatus;
import jodd.servlet.tag.TagUtil;
import jodd.typeconverter.Converter;

public class IteratorTag extends SimpleTagSupport {
	protected Object items;
	protected String var;
	protected String status;
	protected int modulus = 2;
	protected String scope;
	protected int from;
	protected int count = -1;
	protected IteratorStatus iteratorStatus;

	public void setItems(Object items) {
		this.items = items;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setModulus(int modulus) {
		this.modulus = modulus;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void doTag() throws JspException {
		if (this.items != null) {
			JspFragment body = this.getJspBody();
			if (body != null) {
				PageContext pageContext = (PageContext) this.getJspContext();
				if (this.status != null) {
					this.iteratorStatus = new IteratorStatus(this.modulus);
					TagUtil.setScopeAttribute(this.status, this.iteratorStatus, this.scope, pageContext);
				}

				if (this.items instanceof Collection) {
					this.iterateCollection((Collection) this.items, this.from, this.count, pageContext);
				} else if (this.items.getClass().isArray()) {
					this.iterateArray((Object[]) ((Object[]) this.items), this.from, this.count, pageContext);
				} else {
					if (!(this.items instanceof String)) {
						throw new JspException("Provided items are not iterable");
					}

					this.iterateArray(Converter.get().toStringArray(this.items), this.from, this.count, pageContext);
				}

				if (this.status != null) {
					TagUtil.removeScopeAttribute(this.status, this.scope, pageContext);
				}

				TagUtil.removeScopeAttribute(this.var, this.scope, pageContext);
			}
		}
	}

	protected int calculateTo(int from, int count, int size) {
		int to = size;
		if (count != -1) {
			to = from + count;
			if (to > size) {
				to = size;
			}
		}

		return to;
	}

	protected void iterateCollection(Collection collection, int from, int count, PageContext pageContext)
			throws JspException {
		JspFragment body = this.getJspBody();
		Iterator iter = collection.iterator();
		int i = 0;

		for (int to = this.calculateTo(from, count, collection.size()); i < to; ++i) {
			Object item = iter.next();
			if (i >= from) {
				if (this.status != null) {
					this.iteratorStatus.next(!iter.hasNext());
				}

				TagUtil.setScopeAttribute(this.var, item, this.scope, pageContext);
				TagUtil.invokeBody(body);
			}
		}

	}

	protected void iterateArray(Object[] array, int from, int count, PageContext pageContext) throws JspException {
		JspFragment body = this.getJspBody();
		int len = array.length;
		int to = this.calculateTo(from, count, len);
		int last = to - 1;

		for (int i = from; i < to; ++i) {
			Object item = array[i];
			if (this.status != null) {
				this.iteratorStatus.next(i == last);
			}

			TagUtil.setScopeAttribute(this.var, item, this.scope, pageContext);
			TagUtil.invokeBody(body);
		}

	}
}