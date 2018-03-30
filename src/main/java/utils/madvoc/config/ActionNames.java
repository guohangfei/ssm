package utils.madvoc.config;

public class ActionNames {
	private final String packageName;
	private final String packageActionPath;
	private final String className;
	private final String classActionPath;
	private final String methodName;
	private final String methodActionPath;
	private final String httpMethod;

	public ActionNames(String[] packageActionNames, String[] classActionNames, String[] methodActionNames,
			String httpMethod) {
		this.packageName = packageActionNames[0];
		this.packageActionPath = packageActionNames[1];
		this.className = classActionNames[0];
		this.classActionPath = classActionNames[1];
		this.methodName = methodActionNames[0];
		this.methodActionPath = methodActionNames[1];
		this.httpMethod = httpMethod;
	}

	public String packageName() {
		return this.packageName;
	}

	public String packageActionPath() {
		return this.packageActionPath;
	}

	public String className() {
		return this.className;
	}

	public String classActionPath() {
		return this.classActionPath;
	}

	public String methodName() {
		return this.methodName;
	}

	public String methodActionPath() {
		return this.methodActionPath;
	}

	public String httpMethod() {
		return this.httpMethod;
	}
}