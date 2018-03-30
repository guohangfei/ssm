package utils.madvoc.config;

public class ResultPath {
	protected final String path;
	protected final String value;

	public ResultPath(String path, String value) {
		this.path = path;
		this.value = value;
	}

	public String path() {
		return this.path;
	}

	public String value() {
		return this.value;
	}

	public String pathValue() {
		return this.value == null ? this.path : this.path + '.' + this.value;
	}

	public String toString() {
		return this.pathValue();
	}
}