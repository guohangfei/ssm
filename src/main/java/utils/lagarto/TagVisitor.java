package utils.lagarto;

import jodd.lagarto.Doctype;
import jodd.lagarto.Tag;

public interface TagVisitor {
	void start();

	void end();

	void doctype(Doctype arg0);

	void tag(Tag arg0);

	void script(Tag arg0, CharSequence arg1);

	void comment(CharSequence arg0);

	void text(CharSequence arg0);

	void condComment(CharSequence arg0, boolean arg1, boolean arg2, boolean arg3);

	void xml(CharSequence arg0, CharSequence arg1, CharSequence arg2);

	void cdata(CharSequence arg0);

	void error(String arg0);
}