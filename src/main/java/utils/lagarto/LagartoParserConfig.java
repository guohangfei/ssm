package utils.lagarto;

public class LagartoParserConfig<T extends LagartoParserConfig<T>> {
	protected boolean parseXmlTags = false;
	protected boolean enableConditionalComments = true;
	protected boolean caseSensitive = false;
	protected boolean calculatePosition = false;
	protected boolean enableRawTextModes = true;

	public boolean isEnableConditionalComments() {
		return this.enableConditionalComments;
	}

	protected T _this() {
		return this;
	}

	public T setEnableConditionalComments(boolean enableConditionalComments) {
		this.enableConditionalComments = enableConditionalComments;
		return this._this();
	}

	public boolean isParseXmlTags() {
		return this.parseXmlTags;
	}

	public T setParseXmlTags(boolean parseXmlTags) {
		this.parseXmlTags = parseXmlTags;
		return this._this();
	}

	public boolean isCaseSensitive() {
		return this.caseSensitive;
	}

	public T setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		return this._this();
	}

	public boolean isCalculatePosition() {
		return this.calculatePosition;
	}

	public T setCalculatePosition(boolean calculatePosition) {
		this.calculatePosition = calculatePosition;
		return this._this();
	}

	public boolean isEnableRawTextModes() {
		return this.enableRawTextModes;
	}

	public T setEnableRawTextModes(boolean enableRawTextModes) {
		this.enableRawTextModes = enableRawTextModes;
		return this._this();
	}
}