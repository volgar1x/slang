package slang.expressions;

/**
 * @author Antoine Chauvin
 */
public final class QuoteExpression implements ExpressionInterface {
    private final ExpressionInterface expression;

    public QuoteExpression(ExpressionInterface expression) {
        this.expression = expression;
    }

    public ExpressionInterface getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "Quote{" +
                expression +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuoteExpression that = (QuoteExpression) o;

        return expression.equals(that.expression);

    }

    @Override
    public int hashCode() {
        return expression.hashCode();
    }
}
