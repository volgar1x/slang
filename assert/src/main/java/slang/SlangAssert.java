package slang;

import org.junit.AssertFormat;
import slang.expressions.AtomExpression;
import slang.expressions.EvaluationContextInterface;
import slang.expressions.ExpressionInterface;
import slang.expressions.FunctionInterface;
import slang.expressions.visitors.Inspector;
import slang.expressions.visitors.Truthy;

/**
 * @author Antoine Chauvin
 */
public final class SlangAssert {
    private SlangAssert() {}

    public static void load(EvaluationContextInterface loadContext) {
        loadContext.register("assert", (FunctionInterface) (context, arguments) -> {
            ExpressionInterface result = context.evaluate(arguments);

            if (!Truthy.truthy(result)) {
                String message = Inspector.inspect(arguments);
                String expected = Inspector.inspect(arguments.getTail().getHead());
                String actual = Inspector.inspect(context.evaluate(arguments.getTail().getTail().getHead()));

                throw new AssertionError(AssertFormat.format(message, expected, actual));
            }

            return AtomExpression.T;
        });
    }
}
