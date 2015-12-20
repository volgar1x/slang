package slang.expressions;

import java.util.NoSuchElementException;
import java.util.function.BiFunction;

/**
 * @author Antoine Chauvin
 */
public final class NilExpression extends ListExpression {
    public static final NilExpression NIL = new NilExpression();

    private NilExpression() {

    }

    @Override
    public ExpressionInterface getHead() {
        throw new NoSuchElementException();
    }

    @Override
    public ListExpression getTail() {
        return this;
    }

    @Override
    public <T> T foldl(T seed, BiFunction<ExpressionInterface, T, T> function) {
        return seed;
    }

    @Override
    public <R> R visit(Visitor<R> visitor) {
        return visitor.visitNil(this);
    }

    @Override
    public String toString() {
        return "nil";
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
