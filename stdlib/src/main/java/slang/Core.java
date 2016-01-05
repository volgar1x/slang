package slang;

import slang.visitors.Inspector;
import slang.visitors.Truth;

import java.util.stream.Collectors;

/**
 * @author Antoine Chauvin
 */
public final class Core {
    public static void load(EvaluationContextInterface context) {
        Stdlib.loadFn(context, SName.of("let"), Core::let, false);
        Stdlib.loadFn(context, SName.of("def"), Core::def, false);
        Stdlib.loadFn(context, SName.of("cond"), Core::cond, false);
        Stdlib.loadFn(context, SName.of("inspect"), Core::inspect, true);
        Stdlib.loadFn(context, SName.of("fn"), Core::fn, false);
        Stdlib.loadFn(context, SName.of("do"), Core::do_, false);
    }

    public static Object let(EvaluationContextInterface context, SList arguments) {
        SVector bindings = (SVector) arguments.head();
        SList instructions = arguments.tail();

        EvaluationContextInterface subcontext = context.link();
        for (int i = 0; i < bindings.size(); i += 2) {
            SName name = (SName) bindings.get(i);
            Object expression = bindings.get(i + 1);

            subcontext.register(name, subcontext.evaluate(expression));
        }

        return instructions.execute(subcontext);
    }

    public static Object def(EvaluationContextInterface context, SList arguments) {
        SFn function = SFn.fromList(arguments);
        SFunction fn = SFn.tailCallOptimized(function);
        context.register(fn.getFunctionName(), fn);
        return new SQuote(fn.getFunctionName());
    }

    public static Object inspect(EvaluationContextInterface context, SList arguments) {
        return arguments.stream().map(Inspector.INSTANCE).collect(Collectors.joining(" "));
    }

    public static Object cond(EvaluationContextInterface context, SList arguments) {
        for (Object arg : arguments) {
            SList argument = (SList) arg;

            if (Truth.truthy(context.evaluate(argument.head()))) {
                return argument.tail().execute(context);
            }
        }

        return SList.nil;
    }

    public static Object fn(EvaluationContextInterface context, SList arguments) {
        return SFn.fromList(SName.of("%"), arguments);
    }

    public static Object do_(EvaluationContextInterface context, SList arguments) {
        return arguments.execute(context);
    }
}
