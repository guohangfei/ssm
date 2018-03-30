package utils.servlet.tag;

public class IteratorStatus {
	protected final int modulus;
	protected boolean last;
	protected int count;

	public IteratorStatus() {
		this.modulus = 2;
	}

	public IteratorStatus(int modulus) {
		this.modulus = modulus;
	}

	public int getCount() {
		return this.count;
	}

	public int getIndex() {
		return this.count - 1;
	}

	public boolean isEven() {
		return this.count % 2 == 0;
	}

	public boolean isOdd() {
		return this.count % 2 == 1;
	}

	public int modulus(int operand) {
		return this.count % operand;
	}

	public int getModulus() {
		return this.count % this.modulus;
	}

	public boolean isFirst() {
		return this.count == 1;
	}

	public boolean isLast() {
		return this.last;
	}

	protected void next(boolean isLast) {
		++this.count;
		this.last = isLast;
	}

	public String toString() {
		return this.count + ":" + (this.isFirst() ? 'F' : '_') + ':' + (this.last ? 'L' : '_') + ':'
				+ this.getModulus();
	}
}