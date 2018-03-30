package utils.madvoc.config;

import java.util.HashMap;
import java.util.Map;
import utils.madvoc.MadvocException;
import utils.util.ArraysUtil;
import utils.util.StringUtil;

public class RootPackages {
	protected String[] packages;
	protected String[] mappings;
	protected Map<String, String> packagePaths;

	public void reset() {
		this.packages = null;
		this.mappings = null;
		this.packagePaths = null;
	}

	public void addRootPackage(String rootPackage) {
		this.addRootPackage(rootPackage, "");
	}

	public void addRootPackageOf(Class actionClass) {
		this.addRootPackageOf(actionClass, "");
	}

	public void addRootPackage(String rootPackage, String mapping) {
		if (this.packages == null) {
			this.packages = new String[0];
		}

		if (this.mappings == null) {
			this.mappings = new String[0];
		}

		if (mapping.length() > 0) {
			if (!mapping.startsWith("/")) {
				mapping = "/" + mapping;
			}

			if (mapping.endsWith("/")) {
				mapping = StringUtil.substring(mapping, 0, -1);
			}
		}

		for (int i = 0; i < this.packages.length; ++i) {
			if (this.packages[i].equals(rootPackage)) {
				if (this.mappings[i].equals(mapping)) {
					return;
				}

				throw new MadvocException("Different mappings for the same root package: " + rootPackage);
			}
		}

		this.packages = ArraysUtil.append(this.packages, rootPackage);
		this.mappings = ArraysUtil.append(this.mappings, mapping);
	}

	public void addRootPackageOf(Class actionClass, String mapping) {
		this.addRootPackage(actionClass.getPackage().getName(), mapping);
	}

	public String findRootPackageForActionPath(String actionPath) {
		if (this.mappings == null) {
			return null;
		} else {
			int ndx = -1;
			int delta = Integer.MAX_VALUE;

			for (int i = 0; i < this.mappings.length; ++i) {
				String mapping = this.mappings[i];
				boolean found = false;
				if (actionPath.equals(mapping)) {
					found = true;
				} else {
					mapping = mapping + "/";
					if (actionPath.startsWith(mapping)) {
						found = true;
					}
				}

				if (found) {
					int distance = actionPath.length() - mapping.length();
					if (distance < delta) {
						ndx = i;
						delta = distance;
					}
				}
			}

			if (ndx == -1) {
				return null;
			} else {
				return this.packages[ndx];
			}
		}
	}

	public String findPackagePathForActionPackage(String actionPackage) {
		if (this.packages == null) {
			return null;
		} else {
			if (this.packagePaths == null) {
				this.packagePaths = new HashMap();
			}

			String packagePath = (String) this.packagePaths.get(actionPackage);
			if (packagePath != null) {
				return packagePath;
			} else {
				int ndx = -1;
				int delta = Integer.MAX_VALUE;

				String result;
				for (int packageActionPath = 0; packageActionPath < this.packages.length; ++packageActionPath) {
					result = this.packages[packageActionPath];
					if (result.equals(actionPackage)) {
						ndx = packageActionPath;
						delta = 0;
						break;
					}

					result = result + '.';
					if (actionPackage.startsWith(result)) {
						int distanceFromTheRoot = actionPackage.length() - result.length();
						if (distanceFromTheRoot < delta) {
							ndx = packageActionPath;
							delta = distanceFromTheRoot;
						}
					}
				}

				if (ndx == -1) {
					return null;
				} else {
					String arg7 = delta == 0 ? "" : StringUtil.substring(actionPackage, -delta - 1, 0);
					arg7 = arg7.replace('.', '/');
					result = this.mappings[ndx] + arg7;
					this.packagePaths.put(actionPackage, result);
					return result;
				}
			}
		}
	}

	public String toString() {
		if (this.packages == null) {
			return "null";
		} else {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < this.packages.length; ++i) {
				String rootPackage = this.packages[i];
				String mapping = this.mappings[i];
				sb.append(rootPackage).append(" --> ").append(mapping).append("\n");
			}

			return sb.toString();
		}
	}
}