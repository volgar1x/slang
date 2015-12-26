package slang;

import slang.expressions.EvaluationContextInterface;
import slang.expressions.ExpressionInterface;
import slang.expressions.ListExpression;

/**
 * @author Antoine Chauvin
 */
class Code {
    public static ExpressionInterface eval(EvaluationContextInterface context, ListExpression arguments) {
        return arguments.map(context);
    }
}
