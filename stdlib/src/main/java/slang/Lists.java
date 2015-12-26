package slang;

import slang.expressions.*;

import java.math.BigInteger;

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

    public static ExpressionInterface cons(EvaluationContextInterface context, ListExpression list) {
        return new ListExpression.Cons(list.getHead(), ((ListExpression) list.getTail().getHead()));
    }

    public static IntegerExpression len(EvaluationContextInterface context, ListExpression list) {
        return list.getHead().visit(new Visitor<IntegerExpression>() {
            @Override
            public IntegerExpression visitMany(ManyExpressionInterface many) {
                return new IntegerExpression(BigInteger.valueOf(many.length()));
            }

            @Override
            public IntegerExpression otherwise(ExpressionInterface expression) {
                throw new UnsupportedOperationException();
            }
        });
    }
}
