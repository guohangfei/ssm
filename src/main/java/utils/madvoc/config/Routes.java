package utils.madvoc.config;

import java.util.function.Supplier;
import jodd.madvoc.MadvocConfig;
import jodd.madvoc.MadvocException;
import jodd.madvoc.config.ActionRuntime;
import jodd.madvoc.config.RouteChunk;
import jodd.madvoc.macro.PathMacros;
import jodd.util.ClassUtil;
import jodd.util.StringUtil;

public class Routes {
	private static final String ANY_METHOD = "*";
	private final RouteChunk root = new RouteChunk(this, (RouteChunk) null, "");
	private RouteChunk anyMethodChunk;
	private Supplier<MadvocConfig> madvocConfigSupplier;

	public Routes(Supplier<MadvocConfig> madvocConfigSupplier) {
		this.madvocConfigSupplier = madvocConfigSupplier;
	}

	public RouteChunk registerPath(String method, String path) {
		if (method == null) {
			method = "*";
		} else {
			method = method.toUpperCase();
		}

		RouteChunk chunk = this.root.findOrCreateChild(method);
		if (method.equals("*")) {
			this.anyMethodChunk = chunk;
		}

		path = StringUtil.cutSurrounding(path, "/");
		String[] pathChunks = StringUtil.splitc(path, '/');
		String[] arg4 = pathChunks;
		int arg5 = pathChunks.length;

		for (int arg6 = 0; arg6 < arg5; ++arg6) {
			String pathChunk = arg4[arg6];
			chunk = chunk.findOrCreateChild(pathChunk);
		}

		return chunk;
	}

	public ActionRuntime lookup(String method, String[] pathChunks) {
		while (true) {
			ActionRuntime actionRuntime = this._lookup(method, pathChunks);
			if (actionRuntime != null) {
				return actionRuntime;
			}

			String lastPath = pathChunks[pathChunks.length - 1];
			int lastNdx = lastPath.lastIndexOf(46);
			if (lastNdx == -1) {
				return null;
			}

			pathChunks[pathChunks.length - 1] = lastPath.substring(0, lastNdx);
		}
	}

	private ActionRuntime _lookup(String method, String[] pathChunks) {
		if (method != null) {
			method = method.toUpperCase();
			RouteChunk actionRuntime = this.root.findOrCreateChild(method);
			ActionRuntime actionRuntime1 = this.lookupFrom(actionRuntime, pathChunks);
			if (actionRuntime1 != null) {
				return actionRuntime1;
			}
		}

		if (this.anyMethodChunk != null) {
			ActionRuntime actionRuntime2 = this.lookupFrom(this.anyMethodChunk, pathChunks);
			if (actionRuntime2 != null) {
				return actionRuntime2;
			}
		}

		return null;
	}

	private ActionRuntime lookupFrom(RouteChunk chunk, String[] path) {
		RouteChunk[] children = chunk.children();
		if (children == null) {
			return null;
		} else {
			RouteChunk[] arg3 = children;
			int arg4 = children.length;

			for (int arg5 = 0; arg5 < arg4; ++arg5) {
				RouteChunk child = arg3[arg5];
				ActionRuntime matched = this.match(child, path, 0);
				if (matched != null) {
					return matched;
				}
			}

			return null;
		}
	}

	private ActionRuntime match(RouteChunk chunk, String[] path, int ndx) {
		int maxDeep = path.length - 1;
		if (ndx > maxDeep) {
			return null;
		} else if (!chunk.match(path[ndx])) {
			return null;
		} else if (ndx == maxDeep) {
			return chunk.isEndpoint() ? chunk.value() : null;
		} else {
			RouteChunk[] children = chunk.children();
			if (children == null) {
				return null;
			} else {
				RouteChunk[] arg5 = children;
				int arg6 = children.length;

				for (int arg7 = 0; arg7 < arg6; ++arg7) {
					RouteChunk child = arg5[arg7];
					ActionRuntime matched = this.match(child, path, ndx + 1);
					if (matched != null) {
						return matched;
					}
				}

				return null;
			}
		}
	}

	public PathMacros buildActionPathMacros(String actionPath) {
		if (actionPath.isEmpty()) {
			return null;
		} else {
			PathMacros pathMacros = this.createPathMacroInstance();
			return !pathMacros.init(actionPath,
					((MadvocConfig) this.madvocConfigSupplier.get()).getPathMacroSeparators()) ? null : pathMacros;
		}
	}

	private PathMacros createPathMacroInstance() {
		try {
			return (PathMacros) ClassUtil
					.newInstance(((MadvocConfig) this.madvocConfigSupplier.get()).getPathMacroClass());
		} catch (Exception arg1) {
			throw new MadvocException(arg1);
		}
	}
}