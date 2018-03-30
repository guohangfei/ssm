package utils.madvoc.injector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import jodd.madvoc.MadvocException;
import jodd.madvoc.ScopeType;
import jodd.madvoc.config.ActionRuntime;
import jodd.madvoc.config.MethodParam;
import jodd.madvoc.config.ScopeData;
import jodd.madvoc.config.ScopeData.In;
import jodd.madvoc.config.ScopeData.Out;
import jodd.madvoc.injector.Target;import jodd.madvoc.injector.Targets.1;
import jodd.util.ClassUtil;

public class Targets {
	final Target[] targets;
	final ScopeData[][] scopes;

	public Targets(Target target, ScopeData[] scopeData) {
		this.targets = new Target[]{target};
		if (scopeData == null) {
			this.scopes = new ScopeData[ScopeType.values().length][1];
		} else {
			this.scopes = new ScopeData[scopeData.length][1];
			int i = 0;

			for (int scopeDataLength = scopeData.length; i < scopeDataLength; ++i) {
				this.scopes[i][0] = scopeData[i];
			}
		}

	}

	public Targets(ActionRuntime actionRuntime, Object action) {
		this.targets = this.makeTargets(actionRuntime, action);
		this.scopes = actionRuntime.getScopeData();
	}

	public boolean usesScope(ScopeType scopeType) {
		ScopeData[] scopeData = this.scopes[scopeType.value()];
		return scopeData != null;
	}

	public void forEachTarget(Consumer<Target> targetConsumer) {
		Target[] arg1 = this.targets;
		int arg2 = arg1.length;

		for (int arg3 = 0; arg3 < arg2; ++arg3) {
			Target target = arg1[arg3];
			targetConsumer.accept(target);
		}

	}

	public void forEachTargetAndInScopes(ScopeType scopeType, BiConsumer<Target, In> biConsumer) {
		ScopeData[] scopeData = this.scopes[scopeType.value()];
		if (scopeData != null) {
			for (int i = 0; i < this.targets.length; ++i) {
				if (scopeData[i] != null) {
					In[] ins = scopeData[i].in;
					if (ins != null) {
						In[] arg5 = ins;
						int arg6 = ins.length;

						for (int arg7 = 0; arg7 < arg6; ++arg7) {
							In in = arg5[arg7];
							biConsumer.accept(this.targets[i], in);
						}
					}
				}
			}

		}
	}

	public void forEachTargetAndOutScopes(ScopeType scopeType, BiConsumer<Target, Out> biConsumer) {
		ScopeData[] scopeData = this.scopes[scopeType.value()];
		if (scopeData != null) {
			for (int i = 0; i < this.targets.length; ++i) {
				if (scopeData[i] != null) {
					Out[] outs = scopeData[i].out;
					if (outs != null) {
						Out[] arg5 = outs;
						int arg6 = outs.length;

						for (int arg7 = 0; arg7 < arg6; ++arg7) {
							Out out = arg5[arg7];
							biConsumer.accept(this.targets[i], out);
						}
					}
				}
			}

		}
	}

	public Object[] extractParametersValues() {
		Object[] values = new Object[this.targets.length - 1];

		for (int i = 1; i < this.targets.length; ++i) {
			values[i - 1] = this.targets[i].getValue();
		}

		return values;
	}

	protected Target[] makeTargets(ActionRuntime actionRuntime, Object action) {
      if(!actionRuntime.hasArguments()) {
         return new Target[]{new Target(action)};
      } else {
         MethodParam[] methodParams = actionRuntime.getMethodParams();
         Target[] target = new Target[methodParams.length + 1];
         target[0] = new Target(action);

         for(int i = 0; i < methodParams.length; ++i) {
            MethodParam mp = methodParams[i];
            Class type = mp.type();
            Object t;
            if(mp.annotationType() == null) {
               t = new Target(this.createActionMethodArgument(type, action));
            } else if(mp.annotationType() == jodd.madvoc.meta.Out.class) {
               t = new Target(this.createActionMethodArgument(type, action), type);
            } else {
               t = new 1(this, type, action);
            }

            target[i + 1] = (Target)t;
         }

         return target;
      }
   }

	protected Object createActionMethodArgument(Class type, Object action) {
		try {
			if (type.getEnclosingClass() != null && !Modifier.isStatic(type.getModifiers())) {
				Constructor ex = type.getDeclaredConstructor(new Class[]{type.getDeclaringClass()});
				ex.setAccessible(true);
				return ex.newInstance(new Object[]{action});
			} else {
				return ClassUtil.newInstance(type);
			}
		} catch (Exception arg3) {
			throw new MadvocException(arg3);
		}
	}
}