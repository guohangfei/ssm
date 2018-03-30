package utils.lagarto;

import utils.lagarto.Doctype;
import utils.lagarto.Tag;
import utils.lagarto.TagVisitor;

public class TagAdapter implements TagVisitor {
	protected final TagVisitor target;

	public TagAdapter(TagVisitor target) {
		this.target = target;
	}

	public TagVisitor getTarget() {
		return this.target;
	}

	public void start() {
		this.target.start();
	}

	public void end() {
		this.target.end();
	}

	public void tag(Tag tag) {
		this.target.tag(tag);
	}

	public void script(Tag tag, CharSequence body) {
		this.target.script(tag, body);
	}

	public void comment(CharSequence comment) {
		this.target.comment(comment);
	}

	public void text(CharSequence text) {
		this.target.text(text);
	}

	public void cdata(CharSequence cdata) {
		this.target.cdata(cdata);
	}

	public void xml(CharSequence version, CharSequence encoding, CharSequence standalone) {
		this.target.xml(version, encoding, standalone);
	}

	public void doctype(Doctype doctype) {
		this.target.doctype(doctype);
	}

	public void condComment(CharSequence expression, boolean isStartingTag, boolean isHidden, boolean isHiddenEndTag) {
		this.target.condComment(expression, isStartingTag, isHidden, isHiddenEndTag);
	}

	public void error(String message) {
		this.target.error(message);
	}
}