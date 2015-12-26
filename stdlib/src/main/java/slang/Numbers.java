package slang;

import slang.expressions.*;
import slang.expressions.visitors.Inspector;

/**
 * @author Antoine Chauvin
 */
public class Numbers {
    public static NumExpression plus(EvaluationContextInterface context, ListExpression list) {
        return list.<NumExpression>foldl(IntegerExpression.ZERO, (x, acc) -> ((NumExpression) x).plus(acc));
    }

    public static NumExpression minus(EvaluationContextInterface context, ListExpression list) {
        NumExpression head = (NumExpression) list.getHead();
        if (list.getTail().isEmpty()) {
            return head;
        }
        return head.minus(plus(context, list.getTail()));
    }

    public static NumExpression times(EvaluationContextInterface context, ListExpression list) {
        return list.<NumExpression>foldl(IntegerExpression.ONE, (x, acc) -> ((NumExpression) x).times(acc));
    }

    public static NumExpression div(EvaluationContextInterface context, ListExpression list) {
        NumExpression head = (NumExpression) list.getHead();
        if (list.getTail().isEmpty()) {
            return head;
        }
        return head.div(times(context, list.getTail()));
    }

    public static NumExpression pow(EvaluationContextInterface context, ListExpression list) {
        NumExpression lhs = (NumExpression) list.getHead();
        NumExpression rhs = (NumExpression) list.getTail().getHead();
        return lhs.pow(rhs);
    }

    public static IntegerExpression rem(EvaluationContextInterface context, ListExpression list) {
        IntegerExpression lhs = (IntegerExpression) list.getHead();
        IntegerExpression rhs = (IntegerExpression) list.getTail().getHead();
        return new IntegerExpression(lhs.asInteger().remainder(rhs.asInteger()));
    }

    public static DecimalExpression sqrt(EvaluationContextInterface context, ListExpression list) {
        return list.getHead().visit(new Visitor<DecimalExpression>() {
            @Override
            public DecimalExpression visitInteger(IntegerExpression integer) {
                return visitDecimal(new DecimalExpression(integer.asDecimal()));
            }

            @Override
            public DecimalExpression visitDecimal(DecimalExpression decimal) {
                return decimal.sqrt();
            }

            @Override
            public DecimalExpression otherwise(ExpressionInterface expression) {
                throw new UnsupportedOperationException("cannot compute square root of " + Inspector.inspect(expression));
            }
        });
    }
}
