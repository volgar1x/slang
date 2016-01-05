package slang;

import org.junit.AssertFormat;
import slang.visitors.Inspector;
import slang.visitors.Truth;

/**
 * @author Antoine Chauvin
 */
public final class SlangAssert {
    private SlangAssert() {}

    public static void load(EvaluationContextInterface loadContext) {
        loadContext.register(SName.of("assert"), (SFunction) (context, arguments) -> {
            Object result = context.evaluate(arguments);

            if (!Truth.truthy(result)) {
                String message = Inspector.inspect(arguments);
                Object lhs = context.evaluate(arguments.tail().head());
                Object rhs = context.evaluate(arguments.tail().tail().head());

                throw new AssertionError(AssertFormat.format(message, Inspector.inspect(lhs), Inspector.inspect(rhs)));
            }

            return SAtom.of("true");
        });
    }
}
