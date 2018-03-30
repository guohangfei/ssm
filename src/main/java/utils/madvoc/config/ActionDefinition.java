package utils.madvoc.config;

public class ActionDefinition {
	protected final String actionPath;
	protected final String actionMethod;
	protected final String resultBasePath;

	public ActionDefinition(String actionPath, String actionMethod, String resultBasePath) {
		this.actionPath = actionPath;
		this.actionMethod = actionMethod;
		this.resultBasePath = resultBasePath == null ? actionPath : resultBasePath;
	}

	public ActionDefinition(String actionPath, String actionMethod) {
		this.actionPath = actionPath;
		this.actionMethod = actionMethod;
		this.resultBasePath = actionPath;
	}

	public ActionDefinition(String actionPath) {
		this(actionPath, (String) null);
	}

	public String actionPath() {
		return this.actionPath;
	}

	public String actionMethod() {
		return this.actionMethod;
	}

	public String resultBasePath() {
		return this.resultBasePath;
	}
}