package utils.lagarto;

import java.io.IOException;
import utils.lagarto.LagartoException;
import utils.lagarto.Tag;
import utils.lagarto.TagType;
import utils.lagarto.Scanner.Position;
import utils.util.ArraysUtil;
import utils.util.CharSequenceUtil;
import utils.util.net.HtmlEncoder;

class ParsedTag implements Tag {
	private static final CharSequence ATTR_NAME_ID = "id";
	private boolean caseSensitive;
	private boolean rawTag;
	private CharSequence name;
	private int idNdx;
	private TagType type;
	private int attributesCount;
	private CharSequence[] attrNames = new CharSequence[16];
	private CharSequence[] attrValues = new CharSequence[16];
	private int tagStartIndex;
	private int tagLength;
	private String position;
	private int deepLevel;
	private boolean modified;

	public void init(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public void start(int startIndex) {
		this.tagStartIndex = startIndex;
		this.name = null;
		this.idNdx = -1;
		this.attributesCount = 0;
		this.tagLength = 0;
		this.modified = false;
		this.type = TagType.START;
		this.rawTag = false;
	}

	void end(int endIndex) {
		this.tagLength = endIndex - this.tagStartIndex;
		this.modified = false;
	}

	void increaseDeepLevel() {
		++this.deepLevel;
	}

	void decreaseDeepLevel() {
		--this.deepLevel;
	}

	public boolean isCaseSensitive() {
		return this.caseSensitive;
	}

	public boolean isRawTag() {
		return this.rawTag;
	}

	public void setRawTag(boolean isRawTag) {
		this.rawTag = isRawTag;
	}

	public CharSequence getName() {
		return this.name;
	}

	public CharSequence getId() {
		return this.idNdx == -1 ? null : this.attrValues[this.idNdx];
	}

	public TagType getType() {
		return this.type;
	}

	public int getDeepLevel() {
		return this.deepLevel;
	}

	public int getAttributeCount() {
		return this.attributesCount;
	}

	public CharSequence getAttributeName(int index) {
		if (index >= this.attributesCount) {
			throw new IndexOutOfBoundsException();
		} else {
			return this.attrNames[index];
		}
	}

	public CharSequence getAttributeValue(int index) {
		if (index >= this.attributesCount) {
			throw new IndexOutOfBoundsException();
		} else {
			return this.attrValues[index];
		}
	}

	public CharSequence getAttributeValue(CharSequence name) {
		int i = 0;

		while (true) {
			if (i >= this.attributesCount) {
				return null;
			}

			CharSequence current = this.attrNames[i];
			if (this.caseSensitive) {
				if (current.equals(name)) {
					break;
				}
			} else if (CharSequenceUtil.equalsIgnoreCase(current, name)) {
				break;
			}

			++i;
		}

		return this.attrValues[i];
	}

	public int getAttributeIndex(CharSequence name) {
		int i = 0;

		while (true) {
			if (i >= this.attributesCount) {
				return -1;
			}

			CharSequence current = this.attrNames[i];
			if (this.caseSensitive) {
				if (current.equals(name)) {
					break;
				}
			} else if (CharSequenceUtil.equalsIgnoreCase(current, name)) {
				break;
			}

			++i;
		}

		return i;
	}

	public boolean hasAttribute(CharSequence name) {
		return this.getAttributeIndex(name) > -1;
	}

	public int getTagPosition() {
		return this.tagStartIndex;
	}

	public int getTagLength() {
		return this.tagLength;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(Position position) {
		this.position = position.toString();
	}

	public void setName(CharSequence tagName) {
		this.name = tagName;
		this.modified = true;
	}

	public void setType(TagType type) {
		this.type = type;
		this.modified = true;
	}

	public void addAttribute(CharSequence name, CharSequence value) {
		this.ensureLength();
		this.attrNames[this.attributesCount] = name;
		this.setAttrVal(this.attributesCount, name, value);
		++this.attributesCount;
		this.modified = true;
	}

	public void setAttribute(CharSequence name, CharSequence value) {
		int index = this.getAttributeIndex(name);
		if (index == -1) {
			this.addAttribute(name, value);
		} else {
			this.setAttrVal(index, name, value);
		}

		this.modified = true;
	}

	public void setAttributeValue(int index, CharSequence value) {
		if (index >= this.attributesCount) {
			throw new IndexOutOfBoundsException();
		} else {
			this.setAttrVal(index, value);
			this.modified = true;
		}
	}

	public void setAttributeValue(CharSequence name, CharSequence value) {
		int index = this.getAttributeIndex(name);
		if (index != -1) {
			this.setAttrVal(index, name, value);
			this.modified = true;
		}

	}

	public void setAttributeName(int index, CharSequence name) {
		if (index >= this.attributesCount) {
			throw new IndexOutOfBoundsException();
		} else {
			this.attrNames[index] = name;
			this.modified = true;
		}
	}

	public void removeAttribute(int index) {
		if (index >= this.attributesCount) {
			throw new IndexOutOfBoundsException();
		} else {
			System.arraycopy(this.attrNames, index + 1, this.attrNames, index, this.attributesCount - index);
			System.arraycopy(this.attrValues, index + 1, this.attrValues, index, this.attributesCount - index);
			--this.attributesCount;
			this.modified = true;
		}
	}

	public void removeAttribute(CharSequence name) {
		int index = this.getAttributeIndex(name);
		if (index != -1) {
			this.removeAttribute(index);
		}

		this.modified = true;
	}

	public void removeAttributes() {
		this.attributesCount = 0;
	}

	public boolean isModified() {
		return this.modified;
	}

	public boolean nameEquals(CharSequence charSequence) {
		return this.caseSensitive
				? CharSequenceUtil.equals(this.name, charSequence)
				: CharSequenceUtil.equalsIgnoreCase(this.name, charSequence);
	}

	private void ensureLength() {
		if (this.attributesCount + 1 >= this.attrNames.length) {
			this.attrNames = (CharSequence[]) ArraysUtil.resize(this.attrNames, this.attributesCount * 2);
			this.attrValues = (CharSequence[]) ArraysUtil.resize(this.attrValues, this.attributesCount * 2);
		}

	}

	private void setAttrVal(int index, CharSequence name, CharSequence value) {
		if (this.idNdx == -1 && CharSequenceUtil.equalsToLowercase(name, ATTR_NAME_ID)) {
			this.idNdx = index;
		}

		this.attrValues[index] = value;
	}

	private void setAttrVal(int index, CharSequence value) {
		this.attrValues[index] = value;
	}

	private void appendTo(Appendable out) {
		try {
			out.append(this.type.getStartString());
			out.append(this.name);
			if (this.attributesCount > 0) {
				for (int ioex = 0; ioex < this.attributesCount; ++ioex) {
					out.append(' ');
					out.append(this.attrNames[ioex]);
					CharSequence value = this.attrValues[ioex];
					if (value != null) {
						out.append('=').append('\"');
						out.append(HtmlEncoder.attributeDoubleQuoted(value));
						out.append('\"');
					}
				}
			}

			out.append(this.type.getEndString());
		} catch (IOException arg3) {
			throw new LagartoException(arg3);
		}
	}

	public void writeTo(Appendable out) {
		this.appendTo(out);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		this.appendTo(sb);
		return sb.toString();
	}
}