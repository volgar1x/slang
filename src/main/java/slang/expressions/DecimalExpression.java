package slang.expressions;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public DecimalExpression sqrt() {
        BigDecimal sqrt = new BigDecimal(1);
//        sqrt.setScale(scale + 3, RoundingMode.FLOOR);
        BigDecimal store = new BigDecimal(decimal.toString());
        boolean first = true;
        do{
            if (!first){
                store = new BigDecimal(sqrt.toString());
            }
            else first = false;
//            store.setScale(scale + 3, RoundingMode.FLOOR);
            sqrt = decimal.divide(store, RoundingMode.FLOOR).add(store).divide(
                    TWO.decimal, RoundingMode.FLOOR);
        }while (!store.equals(sqrt));
        return new DecimalExpression(sqrt);
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
