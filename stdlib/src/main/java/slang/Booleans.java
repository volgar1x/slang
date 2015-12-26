package slang;

import slang.expressions.*;
import slang.expressions.visitors.Truthy;

/**
 * @author Antoine Chauvin
 */
public class Booleans {
    public static ExpressionInterface not(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(Truthy.truthy(list.getHead()));
    }

    public static ExpressionInterface equals(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(list.getHead().equals(list.getTail().getHead()));
    }

    public static ExpressionInterface nequals(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(!list.getHead().equals(list.getTail().getHead()));
    }

    public static ExpressionInterface gt(EvaluationContextInterface context, ListExpression list) {
        ExpressionInterface lhs = list.getHead();
        ExpressionInterface rhs = list.getTail().getHead();

        if (lhs instanceof Comparable) {
            //noinspection unchecked
            return BooleanExpression.from(((Comparable) lhs).compareTo(rhs) > 0);
        }
        return NilExpression.NIL;
    }

    public static ExpressionInterface gte(EvaluationContextInterface context, ListExpression list) {
        ExpressionInterface lhs = list.getHead();
        ExpressionInterface rhs = list.getTail().getHead();

        if (lhs instanceof Comparable) {
            //noinspection unchecked
            return BooleanExpression.from(((Comparable) lhs).compareTo(rhs) >= 0);
        }
        return NilExpression.NIL;
    }

    public static ExpressionInterface lt(EvaluationContextInterface context, ListExpression list) {
        ExpressionInterface lhs = list.getHead();
        ExpressionInterface rhs = list.getTail().getHead();

        if (lhs instanceof Comparable) {
            //noinspection unchecked
            return BooleanExpression.from(((Comparable) lhs).compareTo(rhs) < 0);
        }
        return NilExpression.NIL;
    }

    public static ExpressionInterface lte(EvaluationContextInterface context, ListExpression list) {
        ExpressionInterface lhs = list.getHead();
        ExpressionInterface rhs = list.getTail().getHead();

        if (lhs instanceof Comparable) {
            //noinspection unchecked
            return BooleanExpression.from(((Comparable) lhs).compareTo(rhs) <= 0);
        }
        return NilExpression.NIL;
    }
}
