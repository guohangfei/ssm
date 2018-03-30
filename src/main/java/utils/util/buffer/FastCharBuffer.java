package utils.util.buffer;

public class FastCharBuffer implements CharSequence, Appendable {
	private char[][] buffers = new char[16][];
	private int buffersCount;
	private int currentBufferIndex = -1;
	private char[] currentBuffer;
	private int offset;
	private int size;
	private final int minChunkLen;

	public FastCharBuffer() {
		this.minChunkLen = 1024;
	}

	public FastCharBuffer(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Invalid size: " + size);
		} else {
			this.minChunkLen = size;
		}
	}

	private void needNewBuffer(int newSize) {
		int delta = newSize - this.size;
		int newBufferSize = Math.max(this.minChunkLen, delta);
		++this.currentBufferIndex;
		this.currentBuffer = new char[newBufferSize];
		this.offset = 0;
		if (this.currentBufferIndex >= this.buffers.length) {
			int newLen = this.buffers.length << 1;
			char[][] newBuffers = new char[newLen][];
			System.arraycopy(this.buffers, 0, newBuffers, 0, this.buffers.length);
			this.buffers = newBuffers;
		}

		this.buffers[this.currentBufferIndex] = this.currentBuffer;
		++this.buffersCount;
	}

	public FastCharBuffer append(char[] array, int off, int len) {
		int end = off + len;
		if (off >= 0 && len >= 0 && end <= array.length) {
			if (len == 0) {
				return this;
			} else {
				int newSize = this.size + len;
				int remaining = len;
				int part;
				if (this.currentBuffer != null) {
					part = Math.min(len, this.currentBuffer.length - this.offset);
					System.arraycopy(array, end - len, this.currentBuffer, this.offset, part);
					remaining = len - part;
					this.offset += part;
					this.size += part;
				}

				if (remaining > 0) {
					this.needNewBuffer(newSize);
					part = Math.min(remaining, this.currentBuffer.length - this.offset);
					System.arraycopy(array, end - remaining, this.currentBuffer, this.offset, part);
					this.offset += part;
					this.size += part;
				}

				return this;
			}
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	public FastCharBuffer append(char[] array) {
		return this.append((char[]) array, 0, array.length);
	}

	public FastCharBuffer append(char element) {
		if (this.currentBuffer == null || this.offset == this.currentBuffer.length) {
			this.needNewBuffer(this.size + 1);
		}

		this.currentBuffer[this.offset] = element;
		++this.offset;
		++this.size;
		return this;
	}

	public FastCharBuffer append(FastCharBuffer buff) {
		if (buff.size == 0) {
			return this;
		} else {
			for (int i = 0; i < buff.currentBufferIndex; ++i) {
				this.append(buff.buffers[i]);
			}

			this.append((char[]) buff.currentBuffer, 0, buff.offset);
			return this;
		}
	}

	public int size() {
		return this.size;
	}

	public boolean isEmpty() {
		return this.size == 0;
	}

	public int index() {
		return this.currentBufferIndex;
	}

	public int offset() {
		return this.offset;
	}

	public char[] array(int index) {
		return this.buffers[index];
	}

	public void clear() {
		this.size = 0;
		this.offset = 0;
		this.currentBufferIndex = -1;
		this.currentBuffer = null;
		this.buffersCount = 0;
	}

	public char[] toArray() {
		int pos = 0;
		char[] array = new char[this.size];
		if (this.currentBufferIndex == -1) {
			return array;
		} else {
			for (int i = 0; i < this.currentBufferIndex; ++i) {
				int len = this.buffers[i].length;
				System.arraycopy(this.buffers[i], 0, array, pos, len);
				pos += len;
			}

			System.arraycopy(this.buffers[this.currentBufferIndex], 0, array, pos, this.offset);
			return array;
		}
	}

	public char[] toArray(int start, int len) {
		int remaining = len;
		int pos = 0;
		char[] array = new char[len];
		if (len == 0) {
			return array;
		} else {
			int i;
			for (i = 0; start >= this.buffers[i].length; ++i) {
				start -= this.buffers[i].length;
			}

			while (i < this.buffersCount) {
				char[] buf = this.buffers[i];
				int c = Math.min(buf.length - start, remaining);
				System.arraycopy(buf, start, array, pos, c);
				pos += c;
				remaining -= c;
				if (remaining == 0) {
					break;
				}

				start = 0;
				++i;
			}

			return array;
		}
	}

	public char get(int index) {
		if (index < this.size && index >= 0) {
			int ndx = 0;

			while (true) {
				char[] b = this.buffers[ndx];
				if (index < b.length) {
					return b[index];
				}

				++ndx;
				index -= b.length;
			}
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	public int length() {
		return this.size;
	}

	public String toString() {
		return new String(this.toArray());
	}

	public char charAt(int index) {
		return this.get(index);
	}

	public CharSequence subSequence(int start, int end) {
		int len = end - start;
		return (new StringBuilder(len)).append(this.toArray(start, len));
	}

	public FastCharBuffer append(String string) {
		int len = string.length();
		if (len == 0) {
			return this;
		} else {
			int newSize = this.size + len;
			int remaining = len;
			int start = 0;
			int part;
			if (this.currentBuffer != null) {
				part = Math.min(len, this.currentBuffer.length - this.offset);
				string.getChars(0, part, this.currentBuffer, this.offset);
				remaining = len - part;
				this.offset += part;
				this.size += part;
				start += part;
			}

			if (remaining > 0) {
				this.needNewBuffer(newSize);
				part = Math.min(remaining, this.currentBuffer.length - this.offset);
				string.getChars(start, start + part, this.currentBuffer, this.offset);
				this.offset += part;
				this.size += part;
			}

			return this;
		}
	}

	public FastCharBuffer append(CharSequence csq) {
		this.append((CharSequence) csq, 0, csq.length());
		return this;
	}

	public FastCharBuffer append(CharSequence csq, int start, int end) {
		for (int i = start; i < end; ++i) {
			this.append(csq.charAt(i));
		}

		return this;
	}
}