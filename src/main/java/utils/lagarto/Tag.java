package utils.lagarto;

import java.io.IOException;
import utils.lagarto.TagType;

public interface Tag {
	boolean isCaseSensitive();

	boolean isRawTag();

	CharSequence getName();

	TagType getType();

	CharSequence getId();

	int getDeepLevel();

	int getAttributeCount();

	CharSequence getAttributeName(int arg0);

	CharSequence getAttributeValue(int arg0);

	CharSequence getAttributeValue(CharSequence arg0);

	int getAttributeIndex(CharSequence arg0);

	boolean hasAttribute(CharSequence arg0);

	int getTagPosition();

	int getTagLength();

	String getPosition();

	void setName(CharSequence arg0);

	void setType(TagType arg0);

	void addAttribute(CharSequence arg0, CharSequence arg1);

	void setAttribute(CharSequence arg0, CharSequence arg1);

	void setAttributeValue(int arg0, CharSequence arg1);

	void setAttributeValue(CharSequence arg0, CharSequence arg1);

	void setAttributeName(int arg0, CharSequence arg1);

	void removeAttribute(int arg0);

	void removeAttribute(CharSequence arg0);

	void removeAttributes();

	boolean isModified();

	boolean nameEquals(CharSequence arg0);

	void writeTo(Appendable arg0) throws IOException;

	String toString();
}