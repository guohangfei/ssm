package utils.lagarto;

public interface Doctype {
	CharSequence getName();

	boolean isQuirksMode();

	CharSequence getPublicIdentifier();

	CharSequence getSystemIdentifier();
}