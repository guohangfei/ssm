package utils.lagarto;

import utils.lagarto.Scanner.Position;
import utils.util.CharArraySequence;
import utils.util.CharUtil;

class Scanner {
	protected char[] input;
	protected int ndx = 0;
	protected int total;
	private int lastOffset = -1;
	private int lastLine;
	private int lastLastNewLineOffset;

	protected void initialize(char[] input) {
		this.input = input;
		this.ndx = -1;
		this.total = input.length;
	}

	protected final int find(char target, int from, int end) {
		while (from < end && this.input[from] != target) {
			++from;
		}

		return from == end ? -1 : from;
	}

	protected final int find(char[] target, int from, int end) {
		while (from < end && !this.match(target, from)) {
			++from;
		}

		return from == end ? -1 : from;
	}

	protected final boolean match(char[] target, int ndx) {
		if (ndx + target.length >= this.total) {
			return false;
		} else {
			int j = ndx;

			for (int i = 0; i < target.length; ++j) {
				if (this.input[j] != target[i]) {
					return false;
				}

				++i;
			}

			return true;
		}
	}

	public final boolean match(char[] target) {
		return this.match(target, this.ndx);
	}

	public final boolean matchUpperCase(char[] uppercaseTarget) {
		if (this.ndx + uppercaseTarget.length > this.total) {
			return false;
		} else {
			int j = this.ndx;

			for (int i = 0; i < uppercaseTarget.length; ++j) {
				char c = CharUtil.toUpperAscii(this.input[j]);
				if (c != uppercaseTarget[i]) {
					return false;
				}

				++i;
			}

			return true;
		}
	}

	protected final CharSequence charSequence(int from, int to) {
		return (CharSequence) (from == to
				? CharArraySequence.EMPTY
				: CharArraySequence.of(this.input, from, to - from));
	}

	protected final boolean isEOF() {
		return this.ndx >= this.total;
	}

	protected Position position(int position) {
		int line;
		int offset;
		int lastNewLineOffset;
		if (position > this.lastOffset) {
			line = 1;
			offset = 0;
			lastNewLineOffset = 0;
		} else {
			line = this.lastLine;
			offset = this.lastOffset;
			lastNewLineOffset = this.lastLastNewLineOffset;
		}

		for (; offset < position; ++offset) {
			char c = this.input[offset];
			if (c == 10) {
				++line;
				lastNewLineOffset = offset + 1;
			}
		}

		this.lastOffset = offset;
		this.lastLine = line;
		this.lastLastNewLineOffset = lastNewLineOffset;
		return new Position(position, line, position - lastNewLineOffset + 1);
	}
}