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
        loadContext.register(SAtom.of("assert"), (SFunction) (context, arguments) -> {
            Object result = context.evaluate(arguments);

            if (!Truth.truthy(result)) {
                String message = Inspector.inspect(arguments);
                String expected = Inspector.inspect(arguments.tail().head());
                String actual = Inspector.inspect(context.evaluate(arguments.tail().tail().head()));

                throw new AssertionError(AssertFormat.format(message, expected, actual));
            }

            return SAtom.of("true");
        });
    }
}
