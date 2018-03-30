package utils.madvoc.config;

import utils.madvoc.config.ActionRuntime;
import utils.madvoc.config.Routes;
import utils.madvoc.macro.PathMacros;
import utils.util.ArraysUtil;

public class RouteChunk {
	private final String value;
	private RouteChunk[] children;
	private final PathMacros pathMacros;
	private final Routes routes;
	private final RouteChunk parent;
	private final boolean hasMacros;
	private ActionRuntime actionRuntime;

	protected RouteChunk(Routes routes, RouteChunk parent, String value) {
		this.routes = routes;
		this.parent = parent;
		this.value = value;
		this.pathMacros = routes.buildActionPathMacros(value);
		if (this.pathMacros != null) {
			this.hasMacros = true;
		} else {
			this.hasMacros = parent != null && parent.hasMacros;
		}

	}

	public RouteChunk add(String newValue) {
		RouteChunk routeChunk = new RouteChunk(this.routes, this, newValue);
		if (this.children == null) {
			this.children = new RouteChunk[]{routeChunk};
		} else {
			this.children = (RouteChunk[]) ArraysUtil.append(this.children, routeChunk);
		}

		return routeChunk;
	}

	public RouteChunk findOrCreateChild(String value) {
		if (this.children != null) {
			RouteChunk[] arg1 = this.children;
			int arg2 = arg1.length;

			for (int arg3 = 0; arg3 < arg2; ++arg3) {
				RouteChunk child = arg1[arg3];
				if (child.get().equals(value)) {
					return child;
				}
			}
		}

		return this.add(value);
	}

	public void bind(ActionRuntime actionRuntime) {
		this.actionRuntime = actionRuntime;
		this.actionRuntime.bind(this);
	}

	public boolean isEndpoint() {
		return this.actionRuntime != null;
	}

	public ActionRuntime value() {
		return this.actionRuntime;
	}

	public String get() {
		return this.value;
	}

	public RouteChunk parent() {
		return this.parent;
	}

	public PathMacros pathMacros() {
		return this.pathMacros;
	}

	public boolean hasMacrosOnPath() {
		return this.hasMacros;
	}

	public RouteChunk[] children() {
		return this.children;
	}

	public boolean match(String value) {
		return this.pathMacros == null ? this.value.equals(value) : this.pathMacros.match(value) != -1;
	}

	public String toString() {
		return "RouteChunk{value=\'" + this.value + '\'' + '}';
	}
}