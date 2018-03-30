package utils.util.inex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import utils.util.inex.InExRuleMatcher;
import utils.util.inex.InExRules.Rule;

public class InExRules<V, D, R> implements InExRuleMatcher<V, R> {
	protected List<Rule<R>> rules;
	protected final InExRuleMatcher<V, R> inExRuleMatcher;
	protected int includesCount;
	protected int excludesCount;
	protected boolean blacklist = true;

	public InExRules<String, String, String> get() {
		return new InExRules();
	}

	public InExRules() {
		this.inExRuleMatcher = this;
	}

	public InExRules(InExRuleMatcher<V, R> inExRuleMatcher) {
		this.inExRuleMatcher = inExRuleMatcher;
	}

	public int totalRules() {
		return this.rules == null ? 0 : this.rules.size();
	}

	public int totalIncludeRules() {
		return this.includesCount;
	}

	public int totalExcludeRules() {
		return this.excludesCount;
	}

	public boolean hasRules() {
		return this.rules == null ? false : !this.rules.isEmpty();
	}

	public R getRule(int index) {
		return ((Rule) this.rules.get(index)).value;
	}

	public void reset() {
		if (this.rules != null) {
			this.rules.clear();
		}

		this.includesCount = this.excludesCount = 0;
		this.blacklist = true;
	}

	public void blacklist() {
		this.blacklist = true;
	}

	public boolean isBlacklist() {
		return this.blacklist;
	}

	public void whitelist() {
		this.blacklist = false;
	}

	public boolean isWhitelist() {
		return !this.blacklist;
	}

	public void smartMode() {
		if (this.excludesCount == 0 && this.includesCount > 0) {
			this.whitelist();
		} else if (this.excludesCount > 0 && this.includesCount == 0) {
			this.blacklist();
		}

	}

	public void include(D rule) {
		this.addRule(rule, true);
	}

	public void exclude(D rule) {
		this.addRule(rule, false);
	}

	protected void addRule(D ruleDefinition, boolean include) {
		if (this.rules == null) {
			this.rules = new ArrayList();
		}

		if (include) {
			++this.includesCount;
		} else {
			++this.excludesCount;
		}

		Rule newRule = new Rule(this.makeRule(ruleDefinition), include);
		if (!this.rules.contains(newRule)) {
			this.rules.add(newRule);
		}
	}

	protected R makeRule(D rule) {
		return rule;
	}

	public boolean match(V value) {
		return this.match(value, this.blacklist);
	}

	public boolean match(V value, boolean blacklist) {
		if (this.rules == null) {
			return blacklist;
		} else {
			boolean include;
			if (blacklist) {
				include = this.processExcludes(value, true);
				include = this.processIncludes(value, include);
			} else {
				include = this.processIncludes(value, false);
				include = this.processExcludes(value, include);
			}

			return include;
		}
	}

	public boolean apply(V value, boolean flag) {
		return this.apply(value, this.blacklist, flag);
	}

	public boolean apply(V value, boolean blacklist, boolean flag) {
		if (this.rules == null) {
			return flag;
		} else {
			if (blacklist) {
				flag = this.processExcludes(value, flag);
				flag = this.processIncludes(value, flag);
			} else {
				flag = this.processIncludes(value, flag);
				flag = this.processExcludes(value, flag);
			}

			return flag;
		}
	}

	protected boolean processIncludes(V value, boolean include) {
		if (this.includesCount > 0 && !include) {
			Iterator arg2 = this.rules.iterator();

			while (arg2.hasNext()) {
				Rule rule = (Rule) arg2.next();
				if (rule.include && this.inExRuleMatcher.accept(value, rule.value, true)) {
					include = true;
					break;
				}
			}
		}

		return include;
	}

	protected boolean processExcludes(V value, boolean include) {
		if (this.excludesCount > 0 && include) {
			Iterator arg2 = this.rules.iterator();

			while (arg2.hasNext()) {
				Rule rule = (Rule) arg2.next();
				if (!rule.include && this.inExRuleMatcher.accept(value, rule.value, false)) {
					include = false;
					break;
				}
			}
		}

		return include;
	}

	public boolean accept(V value, R rule, boolean include) {
		return value.equals(rule);
	}
}