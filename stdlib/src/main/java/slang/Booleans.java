package slang;

import slang.expressions.*;
import slang.expressions.visitors.Truthy;

/**
 * @author Antoine Chauvin
 */
class Booleans {
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

    public static ExpressionInterface atom(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(list.getHead() instanceof AtomExpression || list.getHead() == NilExpression.NIL);
    }

    public static ExpressionInterface decimal(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(list.getHead() instanceof DecimalExpression);
    }

    public static ExpressionInterface function(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(list.getHead() instanceof FunctionInterface);
    }

    public static ExpressionInterface integer(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(list.getHead() instanceof IntegerExpression);
    }

    public static ExpressionInterface list(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(list.getHead() instanceof ListExpression);
    }

    public static ExpressionInterface many(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(list.getHead() instanceof ManyExpressionInterface);
    }

    public static ExpressionInterface nil(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(list.getHead() == NilExpression.NIL);
    }

    public static ExpressionInterface num(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(list.getHead() instanceof NumExpression);
    }

    public static ExpressionInterface set(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(list.getHead() instanceof SetExpression);
    }

    public static ExpressionInterface string(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(list.getHead() instanceof StringExpression);
    }

    public static ExpressionInterface vector(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(list.getHead() instanceof VectorExpression);
    }
}
