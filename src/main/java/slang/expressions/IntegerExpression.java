package slang.expressions;

import java.math.BigInteger;

/**
 * @author Antoine Chauvin
 */
public final class IntegerExpression implements ExpressionInterface {
    private final BigInteger integer;

    public IntegerExpression(BigInteger integer) {
        this.integer = integer;
    }

    public BigInteger getInteger() {
        return integer;
    }

    @Override
    public <R> R visit(Visitor<R> visitor) {
        return visitor.visitInteger(this);
    }

    @Override
    public String toString() {
        return "Integer{" +
                integer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegerExpression that = (IntegerExpression) o;

        return integer.equals(that.integer);

    }

    @Override
    public int hashCode() {
        return integer.hashCode();
    }
}
