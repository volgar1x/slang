package slang.expressions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * @author Antoine Chauvin
 */
public final class SetExpression implements ManyExpressionInterface {
    private final Set<ExpressionInterface> expressions;

    public SetExpression(Set<ExpressionInterface> expressions) {
        this.expressions = Collections.unmodifiableSet(expressions);
    }

    public Set<ExpressionInterface> getExpressions() {
        return expressions;
    }

    public static final class Builder implements ManyExpressionInterface.Builder {
        private final Set<ExpressionInterface> expressions = new HashSet<>();

        @Override
        public void add(ExpressionInterface expression) {
            expressions.add(expression);
        }

        @Override
        public SetExpression build() {
            return new SetExpression(expressions);
        }
    }

    @Override
    public <R> R visit(Visitor<R> visitor) {
        return visitor.visitSet(this);
    }

    @Override
    public <T> T foldl(T seed, BiFunction<ExpressionInterface, T, T> function) {
        T acc = seed;
        for (ExpressionInterface expression : expressions) {
            acc = function.apply(expression, acc);
        }
        return acc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SetExpression that = (SetExpression) o;

        return expressions.equals(that.expressions);
    }

    @Override
    public int hashCode() {
        return expressions.hashCode();
    }

    @Override
    public String toString() {
        return expressions.toString();
    }
}
