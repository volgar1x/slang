package slang.expressions;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Antoine Chauvin
 */
public final class DecimalExpression extends NumExpression {
    public static final DecimalExpression ZERO = new DecimalExpression(BigDecimal.ZERO);
    public static final DecimalExpression ONE = new DecimalExpression(BigDecimal.ZERO);
    public static final DecimalExpression TWO = new DecimalExpression(BigDecimal.valueOf(2));
    public static final DecimalExpression TEN = new DecimalExpression(BigDecimal.TEN);

    private final BigDecimal decimal;

    public DecimalExpression(BigDecimal decimal) {
        this.decimal = decimal;
    }

    public BigDecimal asDecimal() {
        return decimal;
    }

    @Override
    public <R> R visit(Visitor<R> visitor) {
        return visitor.visitDecimal(this);
    }

    @Override
    public NumExpression plus(NumExpression num) {
        return new DecimalExpression(decimal.add(num.asDecimal()));
    }

    @Override
    public NumExpression minus(NumExpression num) {
        return new DecimalExpression(decimal.subtract(num.asDecimal()));
    }

    @Override
    public NumExpression times(NumExpression num) {
        return new DecimalExpression(decimal.multiply(num.asDecimal()));
    }

    @Override
    public NumExpression div(NumExpression num) {
        return new DecimalExpression(decimal.divide(num.asDecimal(), MathContext.UNLIMITED));
    }

    @Override
    public NumExpression pow(NumExpression num) {
        return new DecimalExpression(decimal.pow(num.asDecimal().intValueExact()));
    }

    public DecimalExpression sqrt() {
        if (decimal.scale() <= 16) {
            return new DecimalExpression(new BigDecimal(Math.sqrt(decimal.doubleValue())));
        }

        MathContext mc = MathContext.UNLIMITED;
        BigDecimal g = decimal.divide(TWO.decimal, mc);
        boolean done = false;
        final int maxIterations = mc.getPrecision() + 1;
        for (int i = 0; !done && i < maxIterations; i++) {
            // r = (decimal/g + g) / 2
            BigDecimal r = decimal.divide(g, mc);
            r = r.add(g);
            r = r.divide(TWO.decimal, mc);
            done = r.equals(g);
            g = r;
        }
        return new DecimalExpression(g);
    }

    public DecimalExpression round(int ndigits) {
        return new DecimalExpression(decimal.setScale(ndigits, BigDecimal.ROUND_HALF_UP));
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

        return decimal.compareTo(that.decimal) == 0;

    }

    @Override
    public int hashCode() {
        return decimal.hashCode();
    }
}
