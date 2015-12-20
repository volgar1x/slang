package slang.expressions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * @author Antoine Chauvin
 */
public final class IntegerExpression extends NumExpression {
    public static final IntegerExpression ZERO = new IntegerExpression(BigInteger.ZERO);
    public static final IntegerExpression ONE = new IntegerExpression(BigInteger.ONE);
    public static final IntegerExpression TEN = new IntegerExpression(BigInteger.TEN);

    private final BigInteger integer;

    public IntegerExpression(BigInteger integer) {
        this.integer = integer;
    }

    public BigInteger asInteger() {
        return integer;
    }

    @Override
    public BigDecimal asDecimal() {
        return new BigDecimal(integer);
    }

    @Override
    public <R> R visit(Visitor<R> visitor) {
        return visitor.visitInteger(this);
    }

    @Override
    public NumExpression plus(NumExpression num) {
        if (num instanceof IntegerExpression) {
            return new IntegerExpression(integer.add(((IntegerExpression) num).asInteger()));
        }
        return new DecimalExpression(new BigDecimal(integer).add(num.asDecimal()));
    }

    @Override
    public NumExpression minus(NumExpression num) {
        if (num instanceof IntegerExpression) {
            return new IntegerExpression(integer.subtract(((IntegerExpression) num).asInteger()));
        }
        return new DecimalExpression(new BigDecimal(integer).subtract(num.asDecimal()));
    }

    @Override
    public NumExpression times(NumExpression num) {
        if (num instanceof IntegerExpression) {
            return new IntegerExpression(integer.multiply(((IntegerExpression) num).asInteger()));
        }
        return new DecimalExpression(new BigDecimal(integer).multiply(num.asDecimal()));
    }

    @Override
    public NumExpression div(NumExpression num) {
        if (num instanceof IntegerExpression) {
            return new IntegerExpression(integer.divide(((IntegerExpression) num).asInteger()));
        }
        return new DecimalExpression(new BigDecimal(integer).divide(num.asDecimal(), MathContext.UNLIMITED));
    }

    @Override
    public NumExpression pow(NumExpression num) {
        if (num instanceof IntegerExpression) {
            return new IntegerExpression(integer.pow(((IntegerExpression) num).asInteger().intValueExact()));
        }
        return new DecimalExpression(new BigDecimal(integer).pow(num.asDecimal().intValueExact()));
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
