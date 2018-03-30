package utils.lagarto;

import jodd.lagarto.Doctype;
import jodd.lagarto.Tag;
import jodd.lagarto.TagVisitor;

public class TagVisitorChain implements TagVisitor {
	protected final TagVisitor[] targets;

	public TagVisitorChain(TagVisitor... targets) {
		this.targets = targets;
	}

	public void start() {
		TagVisitor[] arg0 = this.targets;
		int arg1 = arg0.length;

		for (int arg2 = 0; arg2 < arg1; ++arg2) {
			TagVisitor target = arg0[arg2];
			target.start();
		}

	}

	public void end() {
		TagVisitor[] arg0 = this.targets;
		int arg1 = arg0.length;

		for (int arg2 = 0; arg2 < arg1; ++arg2) {
			TagVisitor target = arg0[arg2];
			target.end();
		}

	}

	public void tag(Tag tag) {
		TagVisitor[] arg1 = this.targets;
		int arg2 = arg1.length;

		for (int arg3 = 0; arg3 < arg2; ++arg3) {
			TagVisitor target = arg1[arg3];
			target.tag(tag);
		}

	}

	public void script(Tag tag, CharSequence body) {
		TagVisitor[] arg2 = this.targets;
		int arg3 = arg2.length;

		for (int arg4 = 0; arg4 < arg3; ++arg4) {
			TagVisitor target = arg2[arg4];
			target.script(tag, body);
		}

	}

	public void comment(CharSequence comment) {
		TagVisitor[] arg1 = this.targets;
		int arg2 = arg1.length;

		for (int arg3 = 0; arg3 < arg2; ++arg3) {
			TagVisitor target = arg1[arg3];
			target.comment(comment);
		}

	}

	public void text(CharSequence text) {
		TagVisitor[] arg1 = this.targets;
		int arg2 = arg1.length;

		for (int arg3 = 0; arg3 < arg2; ++arg3) {
			TagVisitor target = arg1[arg3];
			target.text(text);
		}

	}

	public void cdata(CharSequence cdata) {
		TagVisitor[] arg1 = this.targets;
		int arg2 = arg1.length;

		for (int arg3 = 0; arg3 < arg2; ++arg3) {
			TagVisitor target = arg1[arg3];
			target.cdata(cdata);
		}

	}

	public void xml(CharSequence version, CharSequence encoding, CharSequence standalone) {
		TagVisitor[] arg3 = this.targets;
		int arg4 = arg3.length;

		for (int arg5 = 0; arg5 < arg4; ++arg5) {
			TagVisitor target = arg3[arg5];
			target.xml(version, encoding, standalone);
		}

	}

	public void doctype(Doctype doctype) {
		TagVisitor[] arg1 = this.targets;
		int arg2 = arg1.length;

		for (int arg3 = 0; arg3 < arg2; ++arg3) {
			TagVisitor target = arg1[arg3];
			target.doctype(doctype);
		}

	}

	public void condComment(CharSequence expression, boolean isStartingTag, boolean isHidden, boolean isHiddenEndTag) {
		TagVisitor[] arg4 = this.targets;
		int arg5 = arg4.length;

		for (int arg6 = 0; arg6 < arg5; ++arg6) {
			TagVisitor target = arg4[arg6];
			target.condComment(expression, isStartingTag, isHidden, isHiddenEndTag);
		}

	}

	public void error(String message) {
		TagVisitor[] arg1 = this.targets;
		int arg2 = arg1.length;

		for (int arg3 = 0; arg3 < arg2; ++arg3) {
			TagVisitor target = arg1[arg3];
			target.error(message);
		}

	}
}