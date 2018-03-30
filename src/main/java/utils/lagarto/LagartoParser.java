package utils.lagarto;

import jodd.lagarto.LagartoParserConfig;
import jodd.lagarto.ParsedDoctype;
import jodd.lagarto.ParsedTag;
import jodd.lagarto.Scanner;
import jodd.lagarto.State;
import jodd.lagarto.TagType;
import jodd.lagarto.TagVisitor;import jodd.lagarto.LagartoParser.1;import jodd.lagarto.LagartoParser.10;import jodd.lagarto.LagartoParser.11;import jodd.lagarto.LagartoParser.12;import jodd.lagarto.LagartoParser.13;import jodd.lagarto.LagartoParser.14;import jodd.lagarto.LagartoParser.15;import jodd.lagarto.LagartoParser.16;import jodd.lagarto.LagartoParser.17;import jodd.lagarto.LagartoParser.18;import jodd.lagarto.LagartoParser.19;import jodd.lagarto.LagartoParser.2;import jodd.lagarto.LagartoParser.20;import jodd.lagarto.LagartoParser.21;import jodd.lagarto.LagartoParser.22;import jodd.lagarto.LagartoParser.23;import jodd.lagarto.LagartoParser.24;import jodd.lagarto.LagartoParser.25;import jodd.lagarto.LagartoParser.26;import jodd.lagarto.LagartoParser.27;import jodd.lagarto.LagartoParser.28;import jodd.lagarto.LagartoParser.29;import jodd.lagarto.LagartoParser.3;import jodd.lagarto.LagartoParser.30;import jodd.lagarto.LagartoParser.31;import jodd.lagarto.LagartoParser.32;import jodd.lagarto.LagartoParser.33;import jodd.lagarto.LagartoParser.34;import jodd.lagarto.LagartoParser.35;import jodd.lagarto.LagartoParser.36;import jodd.lagarto.LagartoParser.37;import jodd.lagarto.LagartoParser.38;import jodd.lagarto.LagartoParser.39;import jodd.lagarto.LagartoParser.4;import jodd.lagarto.LagartoParser.40;import jodd.lagarto.LagartoParser.41;import jodd.lagarto.LagartoParser.42;import jodd.lagarto.LagartoParser.43;import jodd.lagarto.LagartoParser.44;import jodd.lagarto.LagartoParser.45;import jodd.lagarto.LagartoParser.46;import jodd.lagarto.LagartoParser.47;import jodd.lagarto.LagartoParser.48;import jodd.lagarto.LagartoParser.49;import jodd.lagarto.LagartoParser.5;import jodd.lagarto.LagartoParser.6;import jodd.lagarto.LagartoParser.7;import jodd.lagarto.LagartoParser.8;import jodd.lagarto.LagartoParser.9;
import jodd.lagarto.LagartoParser.ScriptEscape;
import jodd.lagarto.LagartoParser.XmlDeclaration;
import jodd.lagarto.Scanner.Position;
import jodd.util.ArraysUtil;
import jodd.util.CharArraySequence;
import jodd.util.CharUtil;
import jodd.util.UnsafeUtil;
import jodd.util.net.HtmlDecoder;

public class LagartoParser extends jodd.lagarto.Scanner {
	protected TagVisitor visitor;
	protected jodd.lagarto.ParsedTag tag;
	protected ParsedDoctype doctype;
	protected long parsingTime;
	protected LagartoParserConfig config = new LagartoParserConfig();
	protected boolean parsing;
	protected State DATA_STATE = new 1(this);
	protected State TAG_OPEN = new 2(this);
	protected State END_TAG_OPEN = new 3(this);
	protected State TAG_NAME = new 4(this);
	protected State BEFORE_ATTRIBUTE_NAME = new 5(this);
	protected State ATTRIBUTE_NAME = new 6(this);
	protected State AFTER_ATTRIBUTE_NAME = new 7(this);
	protected State BEFORE_ATTRIBUTE_VALUE = new 8(this);
	protected State ATTR_VALUE_UNQUOTED = new 9(this);
	protected State ATTR_VALUE_SINGLE_QUOTED = new 10(this);
	protected State ATTR_VALUE_DOUBLE_QUOTED = new 11(this);
	protected State AFTER_ATTRIBUTE_VALUE_QUOTED = new 12(this);
	protected State SELF_CLOSING_START_TAG = new 13(this);
	protected State BOGUS_COMMENT = new 14(this);
	protected State MARKUP_DECLARATION_OPEN = new 15(this);
	protected int rawTextStart;
	protected int rawTextEnd;
	protected char[] rawTagName;
	protected State RAWTEXT = new 16(this);
	protected State RAWTEXT_LESS_THAN_SIGN = new 17(this);
	protected State RAWTEXT_END_TAG_OPEN = new 18(this);
	protected State RAWTEXT_END_TAG_NAME = new 19(this);
	protected int rcdataTagStart = -1;
	protected char[] rcdataTagName;
	protected State RCDATA = new 20(this);
	protected State RCDATA_LESS_THAN_SIGN = new 21(this);
	protected State RCDATA_END_TAG_OPEN = new 22(this);
	protected State RCDATA_END_TAG_NAME = new 23(this);
	protected int commentStart;
	protected State COMMENT_START = new 24(this);
	protected State COMMENT_START_DASH = new 25(this);
	protected State COMMENT = new 26(this);
	protected State COMMENT_END_DASH = new 27(this);
	protected State COMMENT_END = new 28(this);
	protected State COMMENT_END_BANG = new 29(this);
	protected State DOCTYPE = new 30(this);
	protected State BEFORE_DOCTYPE_NAME = new 31(this);
	protected State DOCTYPE_NAME = new 32(this);
	protected State AFTER_DOCUMENT_NAME = new 33(this);
	protected int doctypeIdNameStart;
	protected State AFTER_DOCTYPE_PUBLIC_KEYWORD = new 34(this);
	protected State BEFORE_DOCTYPE_PUBLIC_IDENTIFIER = new 35(this);
	protected State DOCTYPE_PUBLIC_IDENTIFIER_DOUBLE_QUOTED = new 36(this);
	protected State DOCTYPE_PUBLIC_IDENTIFIER_SINGLE_QUOTED = new 37(this);
	protected State AFTER_DOCTYPE_PUBLIC_IDENTIFIER = new 38(this);
	protected State BETWEEN_DOCTYPE_PUBLIC_AND_SYSTEM_IDENTIFIERS = new 39(this);
	protected State BOGUS_DOCTYPE = new 40(this);
	protected State AFTER_DOCTYPE_SYSTEM_KEYWORD = new 41(this);
	protected State BEFORE_DOCTYPE_SYSTEM_IDENTIFIER = new 42(this);
	protected State DOCTYPE_SYSTEM_IDENTIFIER_DOUBLE_QUOTED = new 43(this);
	protected State DOCTYPE_SYSTEM_IDENTIFIER_SINGLE_QUOTED = new 44(this);
	protected State AFTER_DOCTYPE_SYSTEM_IDENTIFIER = new 45(this);
	protected int scriptStartNdx = -1;
	protected int scriptEndNdx = -1;
	protected int scriptEndTagName = -1;
	protected State SCRIPT_DATA = new 46(this);
	protected State SCRIPT_DATA_LESS_THAN_SIGN = new 47(this);
	protected State SCRIPT_DATA_END_TAG_OPEN = new 48(this);
	protected State SCRIPT_DATA_END_TAG_NAME = new 49(this);
	protected ScriptEscape scriptEscape = null;
	protected XmlDeclaration xmlDeclaration = null;
	protected char[] text;
	protected int textLen;
	protected int attrStartNdx = -1;
	protected int attrEndNdx = -1;
	protected State state;
	private static final char[] TAG_WHITESPACES = new char[]{'\t', '\n', '\r', ' '};
	private static final char[] TAG_WHITESPACES_OR_END = new char[]{'\t', '\n', '\r', ' ', '/', '>'};
	private static final char[] CONTINUE_CHARS = new char[]{'\t', '\n', '\r', ' ', '<', '&'};
	private static final char[] ATTR_INVALID_1 = new char[]{'\"', '\'', '<', '='};
	private static final char[] ATTR_INVALID_2 = new char[]{'\"', '\'', '<'};
	private static final char[] ATTR_INVALID_3 = new char[]{'<', '=', '`'};
	private static final char[] ATTR_INVALID_4 = new char[]{'\"', '\'', '<', '=', '`'};
	private static final char[] COMMENT_DASH = new char[]{'-', '-'};
	private static final char[] T_DOCTYPE = new char[]{'D', 'O', 'C', 'T', 'Y', 'P', 'E'};
	private static final char[] T_SCRIPT = new char[]{'s', 'c', 'r', 'i', 'p', 't'};
	private static final char[] T_XMP = new char[]{'x', 'm', 'p'};
	private static final char[] T_STYLE = new char[]{'s', 't', 'y', 'l', 'e'};
	private static final char[] T_IFRAME = new char[]{'i', 'f', 'r', 'a', 'm', 'e'};
	private static final char[] T_NOFRAMES = new char[]{'n', 'o', 'f', 'r', 'a', 'm', 'e', 's'};
	private static final char[] T_NOEMBED = new char[]{'n', 'o', 'e', 'm', 'b', 'e', 'd'};
	private static final char[] T_NOSCRIPT = new char[]{'n', 'o', 's', 'c', 'r', 'i', 'p', 't'};
	private static final char[] T_TEXTAREA = new char[]{'t', 'e', 'x', 't', 'a', 'r', 'e', 'a'};
	private static final char[] T_TITLE = new char[]{'t', 'i', 't', 'l', 'e'};
	private static final char[] A_PUBLIC = new char[]{'P', 'U', 'B', 'L', 'I', 'C'};
	private static final char[] A_SYSTEM = new char[]{'S', 'Y', 'S', 'T', 'E', 'M'};
	private static final char[] CDATA = new char[]{'[', 'C', 'D', 'A', 'T', 'A', '['};
	private static final char[] CDATA_END = new char[]{']', ']', '>'};
	private static final char[] XML = new char[]{'?', 'x', 'm', 'l'};
	private static final char[] XML_VERSION = new char[]{'v', 'e', 'r', 's', 'i', 'o', 'n'};
	private static final char[] XML_ENCODING = new char[]{'e', 'n', 'c', 'o', 'd', 'i', 'n', 'g'};
	private static final char[] XML_STANDALONE = new char[]{'s', 't', 'a', 'n', 'd', 'a', 'l', 'o', 'n', 'e'};
	private static final char[] CC_IF = new char[]{'[', 'i', 'f', ' '};
	private static final char[] CC_ENDIF = new char[]{'[', 'e', 'n', 'd', 'i', 'f', ']'};
	private static final char[] CC_ENDIF2 = new char[]{'<', '!', '[', 'e', 'n', 'd', 'i', 'f', ']'};
	private static final char[] CC_END = new char[]{']', '>'};
	private static final char[][] RAWTEXT_TAGS;
	private static final char[][] RCDATA_TAGS;
	private static final char REPLACEMENT_CHAR = '�';
	private static final char[] INVALID_CHARS;
	private static final CharSequence _ENDIF;

	public LagartoParser(char[] charArray) {
		this.state = this.DATA_STATE;
		this.initialize(charArray);
	}

	public LagartoParser(String string) {
		this.state = this.DATA_STATE;
		this.initialize(UnsafeUtil.getChars(string));
	}

	protected void initialize(char[] input) {
		super.initialize(input);
		this.tag = new jodd.lagarto.ParsedTag();
		this.doctype = new ParsedDoctype();
		this.text = new char[1024];
		this.textLen = 0;
		this.parsingTime = -1L;
	}

	public LagartoParserConfig getConfig() {
		return this.config;
	}

	public void setConfig(LagartoParserConfig config) {
		this.config = config;
	}

	public void parse(TagVisitor visitor) {
		this.tag.init(this.config.caseSensitive);
		this.parsingTime = System.currentTimeMillis();
		this.visitor = visitor;
		visitor.start();
		this.parsing = true;

		while (this.parsing) {
			this.state.parse();
		}

		this.emitText();
		visitor.end();
		this.parsingTime = System.currentTimeMillis() - this.parsingTime;
	}

	public long getParsingTime() {
		return this.parsingTime;
	}

	protected void consumeCharacterReference(char allowedChar) {
		++this.ndx;
		if (!this.isEOF()) {
			char c = this.input[this.ndx];
			if (c == allowedChar) {
				--this.ndx;
			} else {
				this._consumeAttrCharacterReference();
			}
		}
	}

	protected void consumeCharacterReference() {
		++this.ndx;
		if (!this.isEOF()) {
			this._consumeCharacterReference();
		}
	}

	private void _consumeCharacterReference() {
		int unconsumeNdx = this.ndx - 1;
		char c = this.input[this.ndx];
		if (CharUtil.equalsOne(c, CONTINUE_CHARS)) {
			this.ndx = unconsumeNdx;
			this.textEmitChar('&');
		} else {
			if (c == 35) {
				this._consumeNumber(unconsumeNdx);
			} else {
				String name = HtmlDecoder.detectName(this.input, this.ndx);
				if (name == null) {
					this.errorCharReference();
					this.textEmitChar('&');
					this.ndx = unconsumeNdx;
					return;
				}

				this.ndx += name.length();
				this.textEmitChars(HtmlDecoder.lookup(name));
				c = this.input[this.ndx];
				if (c != 59) {
					this.errorCharReference();
					--this.ndx;
				}
			}

		}
	}

	private void _consumeAttrCharacterReference() {
		int unconsumeNdx = this.ndx - 1;
		char c = this.input[this.ndx];
		if (CharUtil.equalsOne(c, CONTINUE_CHARS)) {
			this.ndx = unconsumeNdx;
			this.textEmitChar('&');
		} else {
			if (c == 35) {
				this._consumeNumber(unconsumeNdx);
			} else {
				String name = HtmlDecoder.detectName(this.input, this.ndx);
				if (name == null) {
					this.errorCharReference();
					this.textEmitChar('&');
					this.ndx = unconsumeNdx;
					return;
				}

				this.ndx += name.length();
				c = this.input[this.ndx];
				if (c == 59) {
					this.textEmitChars(HtmlDecoder.lookup(name));
				} else {
					this.textEmitChar('&');
					this.ndx = unconsumeNdx;
				}
			}

		}
	}

	private void _consumeNumber(int unconsumeNdx) {
		++this.ndx;
		if (this.isEOF()) {
			this.ndx = unconsumeNdx;
		} else {
			char c = this.input[this.ndx];
			int value = 0;
			int digitCount = 0;
			if (c != 88 && c != 120) {
				while (CharUtil.isDigit(c)) {
					value *= 10;
					value += c - 48;
					++this.ndx;
					if (this.isEOF()) {
						this.ndx = unconsumeNdx;
						return;
					}

					c = this.input[this.ndx];
					++digitCount;
				}
			} else {
				label135 : while (true) {
					while (true) {
						++this.ndx;
						if (this.isEOF()) {
							this.ndx = unconsumeNdx;
							return;
						}

						c = this.input[this.ndx];
						if (CharUtil.isDigit(c)) {
							value *= 16;
							value += c - 48;
							++digitCount;
						} else if (c >= 97 && c <= 102) {
							value *= 16;
							value += c - 97 + 10;
							++digitCount;
						} else {
							if (c < 65 || c > 70) {
								break label135;
							}

							value *= 16;
							value += c - 65 + 10;
							++digitCount;
						}
					}
				}
			}

			if (digitCount == 0) {
				this.errorCharReference();
				this.ndx = unconsumeNdx;
			} else {
				if (c != 59) {
					this.errorCharReference();
					--this.ndx;
				}

				boolean isErr = true;
				switch (value) {
					case 0 :
						c = '�';
						break;
					case 128 :
						c = 8364;
						break;
					case 129 :
						c = 129;
						break;
					case 130 :
						c = 8218;
						break;
					case 131 :
						c = 402;
						break;
					case 132 :
						c = 8222;
						break;
					case 133 :
						c = 8230;
						break;
					case 134 :
						c = 8224;
						break;
					case 135 :
						c = 8225;
						break;
					case 136 :
						c = 710;
						break;
					case 137 :
						c = 8240;
						break;
					case 138 :
						c = 352;
						break;
					case 139 :
						c = 8249;
						break;
					case 140 :
						c = 338;
						break;
					case 141 :
						c = 141;
						break;
					case 142 :
						c = 381;
						break;
					case 143 :
						c = 143;
						break;
					case 144 :
						c = 144;
						break;
					case 145 :
						c = 8216;
						break;
					case 146 :
						c = 8217;
						break;
					case 147 :
						c = 8220;
						break;
					case 148 :
						c = 8221;
						break;
					case 149 :
						c = 8226;
						break;
					case 150 :
						c = 8211;
						break;
					case 151 :
						c = 8212;
						break;
					case 152 :
						c = 732;
						break;
					case 153 :
						c = 8482;
						break;
					case 154 :
						c = 353;
						break;
					case 155 :
						c = 8250;
						break;
					case 156 :
						c = 339;
						break;
					case 157 :
						c = 157;
						break;
					case 158 :
						c = 382;
						break;
					case 159 :
						c = 376;
						break;
					default :
						isErr = false;
				}

				if (isErr) {
					this.errorCharReference();
					this.textEmitChar(c);
				} else if ((value < '?' || value > 3583) && value <= 1114111) {
					c = (char) value;
					this.textEmitChar(c);
					if (c >= 1 && c <= 8 || c >= 13 && c <= 31 || c >= 127 && c <= 159 || c >= '﷐' && c <= '﷯') {
						this.errorCharReference();
					} else {
						if (CharUtil.equalsOne(c, INVALID_CHARS)) {
							this.errorCharReference();
						}

					}
				} else {
					this.errorCharReference();
					this.textEmitChar('�');
				}
			}
		}
	}

	private void ensureCapacity() {
		if (this.textLen == this.text.length) {
			this.text = ArraysUtil.resize(this.text, this.textLen << 1);
		}

	}

	private void ensureCapacity(int growth) {
		int desiredLen = this.textLen + growth;
		if (desiredLen > this.text.length) {
			this.text = ArraysUtil.resize(this.text, Math.max(this.textLen << 1, desiredLen));
		}

	}

	protected void textEmitChar(char c) {
		this.ensureCapacity();
		this.text[this.textLen++] = c;
	}

	protected void textStart() {
		this.textLen = 0;
	}

	protected void textEmitChars(int from, int to) {
		this.ensureCapacity(to - from);

		while (from < to) {
			this.text[this.textLen++] = this.input[from++];
		}

	}

	protected void textEmitChars(char[] buffer) {
		this.ensureCapacity(buffer.length);
		char[] arg1 = buffer;
		int arg2 = buffer.length;

		for (int arg3 = 0; arg3 < arg2; ++arg3) {
			char aBuffer = arg1[arg3];
			this.text[this.textLen++] = aBuffer;
		}

	}

	protected CharSequence textWrap() {
		return (CharSequence) (this.textLen == 0 ? CharArraySequence.EMPTY : new String(this.text, 0, this.textLen));
	}

	private void _addAttribute() {
		this._addAttribute(this.charSequence(this.attrStartNdx, this.attrEndNdx), (CharSequence) null);
	}

	private void _addAttributeWithValue() {
		this._addAttribute(this.charSequence(this.attrStartNdx, this.attrEndNdx), this.textWrap().toString());
	}

	private void _addAttribute(CharSequence attrName, CharSequence attrValue) {
		if (this.tag.getType() == TagType.END) {
			this._error("Ignored end tag attribute");
		} else if (this.tag.hasAttribute(attrName)) {
			this._error("Ignored duplicated attribute: " + attrName);
		} else {
			this.tag.addAttribute(attrName, attrValue);
		}

		this.attrStartNdx = -1;
		this.attrEndNdx = -1;
		this.textLen = 0;
	}

	protected void emitTag() {
		this.tag.end(this.ndx + 1);
		if (this.config.calculatePosition) {
			this.tag.setPosition(this.position(this.tag.getTagPosition()));
		}

		if (this.tag.getType().isStartingTag()) {
			if (this.matchTagName(T_SCRIPT)) {
				this.scriptStartNdx = this.ndx + 1;
				this.state = this.SCRIPT_DATA;
				return;
			}

			if (this.config.enableRawTextModes) {
				char[][] arg0 = RAWTEXT_TAGS;
				int arg1 = arg0.length;

				int arg2;
				char[] rcdataTextTagName;
				for (arg2 = 0; arg2 < arg1; ++arg2) {
					rcdataTextTagName = arg0[arg2];
					if (this.matchTagName(rcdataTextTagName)) {
						this.tag.setRawTag(true);
						this.state = this.RAWTEXT;
						this.rawTextStart = this.ndx + 1;
						this.rawTagName = rcdataTextTagName;
						break;
					}
				}

				arg0 = RCDATA_TAGS;
				arg1 = arg0.length;

				for (arg2 = 0; arg2 < arg1; ++arg2) {
					rcdataTextTagName = arg0[arg2];
					if (this.matchTagName(rcdataTextTagName)) {
						this.state = this.RCDATA;
						this.rcdataTagStart = this.ndx + 1;
						this.rcdataTagName = rcdataTextTagName;
						break;
					}
				}
			}

			this.tag.increaseDeepLevel();
		}

		this.visitor.tag(this.tag);
		if (this.tag.getType().isEndingTag()) {
			this.tag.decreaseDeepLevel();
		}

	}

	protected void emitComment(int from, int to) {
		if (this.config.enableConditionalComments) {
			if (this.match(CC_IF, from)) {
				int comment1 = this.find(']', from + 3, to);
				CharSequence expression = this.charSequence(from + 1, comment1);
				this.ndx = comment1 + 1;
				char c = this.input[this.ndx];
				if (c != 62) {
					this.errorInvalidToken();
				}

				this.visitor.condComment(expression, true, true, false);
				this.state = this.DATA_STATE;
				return;
			}

			if (to > CC_ENDIF2.length && this.match(CC_ENDIF2, to - CC_ENDIF2.length)) {
				this.visitor.condComment(_ENDIF, false, true, true);
				this.state = this.DATA_STATE;
				return;
			}
		}

		CharSequence comment = this.charSequence(from, to);
		this.visitor.comment(comment);
		this.commentStart = -1;
	}

	protected void emitText() {
		if (this.textLen != 0) {
			this.visitor.text(this.textWrap());
		}

		this.textLen = 0;
	}

	protected void emitScript(int from, int to) {
		this.tag.increaseDeepLevel();
		this.tag.setRawTag(true);
		this.visitor.script(this.tag, this.charSequence(from, to));
		this.tag.decreaseDeepLevel();
		this.scriptStartNdx = -1;
		this.scriptEndNdx = -1;
	}

	protected void emitDoctype() {
		this.visitor.doctype(this.doctype);
		this.doctype.reset();
	}

	protected void emitXml() {
		this.visitor.xml(this.xmlDeclaration.version, this.xmlDeclaration.encoding, this.xmlDeclaration.standalone);
		this.xmlDeclaration.reset();
	}

	protected void emitCData(CharSequence charSequence) {
		this.visitor.cdata(charSequence);
	}

	protected void errorEOF() {
		this._error("Parse error: EOF");
	}

	protected void errorInvalidToken() {
		this._error("Parse error: invalid token");
	}

	protected void errorCharReference() {
		this._error("Parse error: invalid character reference");
	}

	protected void _error(String message) {
		if (this.config.calculatePosition) {
			Position currentPosition = this.position(this.ndx);
			message = message.concat(" ").concat(currentPosition.toString());
		} else {
			message = message.concat(" [@").concat(Integer.toString(this.ndx)).concat("]");
		}

		this.visitor.error(message);
	}

	private boolean isAppropriateTagName(char[] lowerCaseNameToMatch, int from, int to) {
		int len = to - from;
		if (len != lowerCaseNameToMatch.length) {
			return false;
		} else {
			int i = from;

			for (int k = 0; i < to; ++k) {
				char c = this.input[i];
				c = CharUtil.toLowerAscii(c);
				if (c != lowerCaseNameToMatch[k]) {
					return false;
				}

				++i;
			}

			return true;
		}
	}

	private boolean matchTagName(char[] tagNameLowercase) {
		CharSequence charSequence = this.tag.getName();
		int length = tagNameLowercase.length;
		if (charSequence.length() != length) {
			return false;
		} else {
			for (int i = 0; i < length; ++i) {
				char c = charSequence.charAt(i);
				c = CharUtil.toLowerAscii(c);
				if (c != tagNameLowercase[i]) {
					return false;
				}
			}

			return true;
		}
	}

	static {
		RAWTEXT_TAGS = new char[][]{T_XMP, T_STYLE, T_IFRAME, T_NOEMBED, T_NOFRAMES, T_NOSCRIPT, T_SCRIPT};
		RCDATA_TAGS = new char[][]{T_TEXTAREA, T_TITLE};
		INVALID_CHARS = new char[]{'', '￾', '￿'};
		_ENDIF = "endif";
	}
}