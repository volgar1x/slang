package slang.expressions;

import java.math.BigDecimal;

/**
 * @author Antoine Chauvin
 */
public final class DecimalExpression implements ExpressionInterface {
    private final BigDecimal decimal;

    public DecimalExpression(BigDecimal decimal) {
        this.decimal = decimal;
    }

    public BigDecimal getDecimal() {
        return decimal;
    }

    @Override
    public <R> R visit(Visitor<R> visitor) {
        return visitor.visitDecimal(this);
    }

    @Override
    public String toString() {
        return "Decimal{" +
                decimal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DecimalExpression that = (DecimalExpression) o;

        return decimal.equals(that.decimal);

    }

    @Override
    public int hashCode() {
        return decimal.hashCode();
    }
}
