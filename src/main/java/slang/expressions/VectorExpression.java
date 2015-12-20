package slang.expressions;

import java.util.Arrays;

/**
 * @author Antoine Chauvin
 */
public final class VectorExpression implements ManyExpressionInterface {
    private final ExpressionInterface[] expressions;

    public VectorExpression(ExpressionInterface[] expressions) {
        this.expressions = expressions;
    }

    public static class Builder implements ManyExpressionInterface.Builder {
        private ExpressionInterface[] expressions;
        private int count;

        public Builder(int cap) {
            this.expressions = new ExpressionInterface[cap];
            this.count = 0;
        }

        public Builder() {
            this(10);
        }

        @Override
        public void add(ExpressionInterface expression) {
            if (count == expressions.length) {
                ExpressionInterface[] cpy = new ExpressionInterface[expressions.length * 2];
                System.arraycopy(expressions, 0, cpy, 0, expressions.length);
                expressions = cpy;
            }
            expressions[count++] = expression;
        }

        @Override
        public VectorExpression build() {
            ExpressionInterface[] expressions = new ExpressionInterface[count];
            System.arraycopy(this.expressions, 0, expressions, 0, count);
            return new VectorExpression(expressions);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VectorExpression that = (VectorExpression) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(expressions, that.expressions);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(expressions);
    }

    @Override
    public String toString() {
        return "Vector" + Arrays.toString(expressions);
    }
}
