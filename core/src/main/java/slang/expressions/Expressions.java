package slang.expressions;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Antoine Chauvin
 */
public final class Expressions {
    private Expressions() {}

    public static StringExpression string(String string) {
        return new StringExpression(string);
    }

    public static IntegerExpression integer(String integer) {
        return new IntegerExpression(new BigInteger(integer, 10));
    }

    public static IntegerExpression integer(long integer) {
        return new IntegerExpression(BigInteger.valueOf(integer));
    }

    public static DecimalExpression decimal(String decimal) {
        return new DecimalExpression(new BigDecimal(decimal));
    }

    public static DecimalExpression decimal(double decimal) {
        return new DecimalExpression(BigDecimal.valueOf(decimal));
    }

    public static AtomExpression atom(String atom) {
        return new AtomExpression(atom);
    }

    public static ListExpression list(ExpressionInterface... expressions) {
        return ListExpression.of(expressions);
    }

    public static UnquoteExpression unquote(String name) {
        return new UnquoteExpression(name);
    }

    public static QuoteExpression quote(ExpressionInterface expression) {
        return new QuoteExpression(expression);
    }

    public static SetExpression set(ExpressionInterface... expressions) {
        SetExpression.Builder builder = new SetExpression.Builder();
        for (ExpressionInterface expression : expressions) {
            builder.add(expression);
        }
        return builder.build();
    }

    public static VectorExpression vector(ExpressionInterface... expressions) {
        VectorExpression.Builder builder = new VectorExpression.Builder();
        for (ExpressionInterface expression : expressions) {
            builder.add(expression);
        }
        return builder.build();
    }
}
