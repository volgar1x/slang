package slang.interpreter;

import slang.expressions.AtomExpression;
import slang.expressions.EvaluationContextInterface;
import slang.expressions.ExpressionInterface;
import slang.expressions.FunctionInterface;
import slang.expressions.visitors.Inspector;
import slang.expressions.visitors.Truthy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Antoine Chauvin
 */
public final class SlangAssert {
    private SlangAssert() {}

    public static void load(EvaluationContextInterface loadContext) {
        loadContext.register("assert", (FunctionInterface) (context, arguments) -> {
            if (arguments.getHead().equals(new AtomExpression("="))) {
                ExpressionInterface result = context.evaluate(arguments.getTail().getTail().getHead());
                assertEquals(Inspector.inspect(arguments), arguments.getTail().getHead(), result);
                return result;
            } else {
                ExpressionInterface result = context.evaluate(arguments);
                assertTrue(Inspector.inspect(arguments), Truthy.truthy(result));
                return result;
            }
        });
    }
}
