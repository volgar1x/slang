package slang;

import slang.expressions.EvaluationContextInterface;
import slang.expressions.ExpressionInterface;
import slang.expressions.ListExpression;

/**
 * @author Antoine Chauvin
 */
class Lists {
    public static ExpressionInterface car(EvaluationContextInterface context, ListExpression list) {
        return ((ListExpression) list.getHead()).getHead();
    }

    public static ExpressionInterface cdr(EvaluationContextInterface context, ListExpression list) {
        return ((ListExpression) list.getHead()).getTail();
    }
}
