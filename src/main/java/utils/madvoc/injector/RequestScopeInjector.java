package utils.madvoc.injector;

import java.util.Enumeration;
import java.util.function.BiConsumer;
import javax.servlet.http.HttpServletRequest;
import jodd.io.upload.FileUpload;
import jodd.madvoc.ActionRequest;
import jodd.madvoc.MadvocConfig;
import jodd.madvoc.ScopeType;
import jodd.madvoc.injector.Injector;
import jodd.madvoc.injector.Outjector;
import jodd.madvoc.injector.Targets;
import jodd.servlet.ServletUtil;
import jodd.servlet.upload.MultipartRequestWrapper;
import jodd.util.StringUtil;

public class RequestScopeInjector implements Injector, Outjector {
	private static final ScopeType SCOPE_TYPE;
	protected final String encoding;
	protected boolean ignoreEmptyRequestParams;
	protected boolean treatEmptyParamsAsNull;
	protected boolean injectAttributes = true;
	protected boolean injectParameters = true;
	protected boolean encodeGetParams;
	protected boolean ignoreInvalidUploadFiles = true;

	public RequestScopeInjector(MadvocConfig madvocConfig) {
		this.encoding = madvocConfig.getEncoding();
	}

	public void setIgnoreEmptyRequestParams(boolean ignoreEmptyRequestParams) {
		this.ignoreEmptyRequestParams = ignoreEmptyRequestParams;
	}

	public void setTreatEmptyParamsAsNull(boolean treatEmptyParamsAsNull) {
		this.treatEmptyParamsAsNull = treatEmptyParamsAsNull;
	}

	public void setInjectAttributes(boolean injectAttributes) {
		this.injectAttributes = injectAttributes;
	}

	public void setInjectParameters(boolean injectParameters) {
		this.injectParameters = injectParameters;
	}

	public void setEncodeGetParams(boolean encodeGetParams) {
		this.encodeGetParams = encodeGetParams;
	}

	public void setIgnoreInvalidUploadFiles(boolean ignoreInvalidUploadFiles) {
		this.ignoreInvalidUploadFiles = ignoreInvalidUploadFiles;
	}

	protected void injectAttributes(Targets targets, HttpServletRequest servletRequest) {
		Enumeration attributeNames = servletRequest.getAttributeNames();

		while (attributeNames.hasMoreElements()) {
			String attrName = (String) attributeNames.nextElement();
			targets.forEachTargetAndInScopes(SCOPE_TYPE, (target, in) -> {
				String name = in.matchedPropertyName(attrName);
				if (name != null) {
					Object attrValue = servletRequest.getAttribute(attrName);
					target.writeValue(name, attrValue, true);
				}

			});
		}

	}

	protected void injectParameters(Targets targets, HttpServletRequest servletRequest) {
		boolean encode = this.encodeGetParams && servletRequest.getMethod().equals("GET");
		Enumeration paramNames = servletRequest.getParameterNames();

		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if (servletRequest.getAttribute(paramName) == null) {
				targets.forEachTargetAndInScopes(SCOPE_TYPE, (target, in) -> {
					String name = in.matchedPropertyName(paramName);
					if (name != null) {
						String[] paramValues = servletRequest.getParameterValues(paramName);
						paramValues = ServletUtil.prepareParameters(paramValues, this.treatEmptyParamsAsNull,
								this.ignoreEmptyRequestParams);
						if (paramValues != null) {
							if (encode) {
								for (int value = 0; value < paramValues.length; ++value) {
									String p = paramValues[value];
									if (p != null) {
										paramValues[value] = StringUtil.convertCharset(p, "ISO-8859-1", this.encoding);
									}
								}
							}

							Object arg9 = paramValues.length != 1 ? paramValues : paramValues[0];
							target.writeValue(name, arg9, true);
						}
					}

				});
			}
		}

	}

	protected void injectUploadedFiles(Targets targets, HttpServletRequest servletRequest) {
		if (servletRequest instanceof MultipartRequestWrapper) {
			MultipartRequestWrapper multipartRequest = (MultipartRequestWrapper) servletRequest;
			if (multipartRequest.isMultipart()) {
				Enumeration paramNames = multipartRequest.getFileParameterNames();

				while (paramNames.hasMoreElements()) {
					String paramName = (String) paramNames.nextElement();
					if (servletRequest.getAttribute(paramName) == null) {
						targets.forEachTargetAndInScopes(SCOPE_TYPE, (target, in) -> {
							String name = in.matchedPropertyName(paramName);
							if (name != null) {
								FileUpload[] paramValues = multipartRequest.getFiles(paramName);
								if (this.ignoreInvalidUploadFiles) {
									for (int value = 0; value < paramValues.length; ++value) {
										FileUpload paramValue = paramValues[value];
										if (!paramValue.isValid() || !paramValue.isUploaded()) {
											paramValues[value] = null;
										}
									}
								}

								Object arg8 = paramValues.length == 1 ? paramValues[0] : paramValues;
								target.writeValue(name, arg8, true);
							}

						});
					}
				}

			}
		}
	}

	public void inject(ActionRequest actionRequest) {
		Targets targets = actionRequest.getTargets();
		if (targets.usesScope(SCOPE_TYPE)) {
			HttpServletRequest servletRequest = actionRequest.getHttpServletRequest();
			if (this.injectAttributes) {
				this.injectAttributes(targets, servletRequest);
			}

			if (this.injectParameters) {
				this.injectParameters(targets, servletRequest);
				this.injectUploadedFiles(targets, servletRequest);
			}

		}
	}

	public void outject(ActionRequest actionRequest) {
		Targets targets = actionRequest.getTargets();
		if (targets.usesScope(SCOPE_TYPE)) {
			HttpServletRequest servletRequest = actionRequest.getHttpServletRequest();
			targets.forEachTargetAndOutScopes(SCOPE_TYPE, (target, out) -> {
				Object value = target.readTargetProperty(out);
				servletRequest.setAttribute(out.name, value);
			});
		}
	}

	static {
		SCOPE_TYPE = ScopeType.REQUEST;
	}
}