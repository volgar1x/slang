package slang.expressions;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Antoine Chauvin
 */
public abstract class ListExpression implements ManyExpressionInterface {
    public abstract ExpressionInterface getHead();
    public abstract ListExpression getTail();

    @Override
    public <R> R visit(Visitor<R> visitor) {
        return visitor.visitList(this);
    }

    public ListExpression reverse() {
        return foldl(NilExpression.NIL, Cons::new);
    }

    public ListExpression map(Function<ExpressionInterface, ExpressionInterface> function) {
        return foldl((ListExpression) NilExpression.NIL, (x, acc) -> new Cons(function.apply(x), acc)).reverse();
    }

    public static final class Cons extends ListExpression {
        private final ExpressionInterface head;
        private final ListExpression tail;

        public Cons(ExpressionInterface head, ListExpression tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public ExpressionInterface getHead() {
            return head;
        }

        @Override
        public ListExpression getTail() {
            return tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public <T> T foldl(T seed, BiFunction<ExpressionInterface, T, T> function) {
            return tail.foldl(function.apply(head, seed), function);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cons cons = (Cons) o;
            return head.equals(cons.head) && tail.equals(cons.tail);
        }

        @Override
        public int hashCode() {
            int result = head.hashCode();
            result = 31 * result + tail.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return head.toString() + (tail != NilExpression.NIL ? " :: " + tail.toString() : "");
        }
    }

    public static final class Builder implements ManyExpressionInterface.Builder {
        private ListExpression list = NilExpression.NIL;

        @Override
        public void add(ExpressionInterface expression) {
            list = new Cons(expression, list);
        }

        @Override
        public ListExpression build() {
            return list.reverse();
        }
    }

    public static ListExpression of(ExpressionInterface... expressions) {
        Builder builder = new Builder();
        for (ExpressionInterface expression : expressions) {
            builder.add(expression);
        }
        return builder.build();
    }
}
