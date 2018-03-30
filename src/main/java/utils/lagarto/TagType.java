package utils.lagarto;

public enum TagType {
	START("<", ">"), END("</", ">"), SELF_CLOSING("<", "/>");

	private final String startString;
	private final String endString;
	private final boolean isStarting;
	private final boolean isEnding;

	private TagType(String startString, String endString) {
		this.startString = startString;
		this.endString = endString;
		this.isStarting = startString.length() == 1;
		this.isEnding = startString.length() == 2 || endString.length() == 2;
	}

	public String getStartString() {
		return this.startString;
	}

	public String getEndString() {
		return this.endString;
	}

	public boolean isStartingTag() {
		return this.isStarting;
	}

	public boolean isEndingTag() {
		return this.isEnding;
	}
}