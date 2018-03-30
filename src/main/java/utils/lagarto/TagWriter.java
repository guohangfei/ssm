package utils.lagarto;

import java.io.IOException;
import jodd.lagarto.Doctype;
import jodd.lagarto.LagartoException;
import jodd.lagarto.Tag;
import jodd.lagarto.TagVisitor;
import jodd.lagarto.TagWriterUtil;
import jodd.util.net.HtmlEncoder;

public class TagWriter implements TagVisitor {
	protected Appendable appendable;

	public TagWriter(Appendable appendable) {
		this.appendable = appendable;
	}

	public void setOutput(Appendable out) {
		this.appendable = out;
	}

	public Appendable getOutput() {
		return this.appendable;
	}

	public void start() {
	}

	public void end() {
	}

	public void tag(Tag tag) {
		try {
			tag.writeTo(this.appendable);
		} catch (IOException arg2) {
			throw new LagartoException(arg2);
		}
	}

	public void script(Tag tag, CharSequence body) {
		try {
			tag.writeTo(this.appendable);
			if (body != null) {
				this.appendable.append(body);
			}

			this.appendable.append("</script>");
		} catch (IOException arg3) {
			throw new LagartoException(arg3);
		}
	}

	public void comment(CharSequence comment) {
		try {
			TagWriterUtil.writeComment(this.appendable, comment);
		} catch (IOException arg2) {
			throw new LagartoException(arg2);
		}
	}

	public void text(CharSequence text) {
		try {
			this.appendable.append(HtmlEncoder.text(text));
		} catch (IOException arg2) {
			throw new LagartoException(arg2);
		}
	}

	public void cdata(CharSequence cdata) {
		try {
			TagWriterUtil.writeCData(this.appendable, cdata);
		} catch (IOException arg2) {
			throw new LagartoException(arg2);
		}
	}

	public void xml(CharSequence version, CharSequence encoding, CharSequence standalone) {
		try {
			TagWriterUtil.writeXml(this.appendable, version, encoding, standalone);
		} catch (IOException arg4) {
			throw new LagartoException(arg4);
		}
	}

	public void doctype(Doctype doctype) {
		try {
			TagWriterUtil.writeDoctype(this.appendable, doctype.getName(), doctype.getPublicIdentifier(),
					doctype.getSystemIdentifier());
		} catch (IOException arg2) {
			throw new LagartoException(arg2);
		}
	}

	public void condComment(CharSequence expression, boolean isStartingTag, boolean isHidden, boolean isHiddenEndTag) {
		try {
			TagWriterUtil.writeConditionalComment(this.appendable, expression, isStartingTag, isHidden, isHiddenEndTag);
		} catch (IOException arg5) {
			throw new LagartoException(arg5);
		}
	}

	public void error(String message) {
	}
}