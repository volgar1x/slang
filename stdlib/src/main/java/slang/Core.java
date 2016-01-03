package slang;

import slang.visitors.Inspector;
import slang.visitors.Truth;

import java.util.stream.Collectors;

/**
 * @author Antoine Chauvin
 */
public final class Core {
    public static void load(EvaluationContextInterface context) {
        Stdlib.loadFn(context, SAtom.of("let"), Core::let, false);
        Stdlib.loadFn(context, SAtom.of("def"), Core::def, false);
        Stdlib.loadFn(context, SAtom.of("cond"), Core::cond, false);
        Stdlib.loadFn(context, SAtom.of("inspect"), Core::inspect, true);
    }

    public static Object let(EvaluationContextInterface context, SList arguments) {
        SVector bindings = (SVector) arguments.head();
        SList instructions = arguments.tail();

        EvaluationContextInterface subcontext = context.link();
        for (int i = 0; i < bindings.size(); i += 2) {
            SAtom name = (SAtom) bindings.get(i);
            Object expression = bindings.get(i + 1);

            subcontext.register(name, subcontext.evaluate(expression));
        }

        return instructions.execute(subcontext);
    }

    public static Object def(EvaluationContextInterface context, SList arguments) {
        SFn function = SFn.fromList(arguments);
        context.register(function.getFunctionName(), function);
        return new SQuote(function.getFunctionName());
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
}
