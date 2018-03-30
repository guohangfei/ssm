package utils.lagarto;

import jodd.lagarto.Doctype;

public class ParsedDoctype implements Doctype {
	protected CharSequence name;
	protected CharSequence publicIdentifier;
	protected CharSequence systemIdentifier;
	protected boolean quirksMode;

	public void setName(CharSequence name) {
		this.name = name;
	}

	public void setQuirksMode(boolean quirksMode) {
		this.quirksMode = quirksMode;
	}

	public void reset() {
		this.name = null;
		this.quirksMode = false;
		this.publicIdentifier = null;
		this.systemIdentifier = null;
	}

	public void setPublicIdentifier(CharSequence publicIdentifier) {
		this.publicIdentifier = publicIdentifier;
	}

	public void setSystemIdentifier(CharSequence systemIdentifier) {
		this.systemIdentifier = systemIdentifier;
	}

	public CharSequence getName() {
		return this.name;
	}

	public boolean isQuirksMode() {
		return this.quirksMode;
	}

	public CharSequence getPublicIdentifier() {
		return this.publicIdentifier;
	}

	public CharSequence getSystemIdentifier() {
		return this.systemIdentifier;
	}
}