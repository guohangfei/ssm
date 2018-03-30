package utils.util.inex;

import utils.util.Wildcard;

public interface InExRuleMatcher<T, R> {
	InExRuleMatcher<String, String> WILDCARD_RULE_MATCHER = (value, rule, include) -> {
		return Wildcard.match(value, rule);
	};
	InExRuleMatcher<String, String> WILDCARD_PATH_RULE_MATCHER = (value, rule, include) -> {
		return Wildcard.matchPath(value, rule);
	};

	boolean accept(T arg0, R arg1, boolean arg2);
}