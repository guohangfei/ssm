package utils.madvoc.interceptor;

import jodd.madvoc.ActionRequest;
import jodd.madvoc.interceptor.ActionInterceptor;
import jodd.util.StringUtil;

public class EchoInterceptor implements ActionInterceptor {
	protected String prefixIn = "-----> ";
	protected String prefixOut = "<----- ";

	public void setPrefixIn(String prefixIn) {
		this.prefixIn = prefixIn;
	}

	public void setPrefixOut(String prefixOut) {
		this.prefixOut = prefixOut;
	}

	public Object intercept(ActionRequest actionRequest) throws Exception {
		this.printBefore(actionRequest);
		long startTime = System.currentTimeMillis();
		Object result = null;
		boolean arg12 = false;

		try {
			arg12 = true;
			result = actionRequest.invoke();
			arg12 = false;
		} catch (Exception arg13) {
			result = "<exception>";
			throw arg13;
		} catch (Throwable arg14) {
			result = "<throwable>";
			throw new Exception(arg14);
		} finally {
			if (arg12) {
				long executionTime = System.currentTimeMillis() - startTime;
				this.printAfter(actionRequest, executionTime, result);
			}
		}

		long th = System.currentTimeMillis() - startTime;
		this.printAfter(actionRequest, th, result);
		return result;
	}

	protected void printBefore(ActionRequest request) {
		StringBuilder message = new StringBuilder(this.prefixIn);
		message.append(request.getActionPath()).append("   [").append(request.getActionRuntime().createActionString())
				.append(']');
		this.out(message.toString());
	}

	protected void printAfter(ActionRequest request, long executionTime, Object result) {
		StringBuilder message = new StringBuilder(this.prefixOut);
		String resultString = StringUtil.toSafeString(result);
		if (resultString.length() > 70) {
			resultString = resultString.substring(0, 70);
			resultString = resultString + "...";
		}

		message.append(request.getActionPath()).append("  (").append(resultString).append(") in ").append(executionTime)
				.append("ms.");
		this.out(message.toString());
	}

	protected void out(String message) {
		System.out.println(message);
	}
}