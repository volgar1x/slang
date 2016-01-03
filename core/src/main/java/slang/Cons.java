package slang;

import java.util.AbstractList;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Antoine Chauvin
 */
public final class Cons extends AbstractList<Object> implements SList {
    private final Object head;
    private final SList tail;

    public Cons(Object head, SList tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public Object get(int index) {
        if (index == 0) {
            return head;
        }
        return tail.get(index - 1);
    }

    @Override
    public int size() {
        return 1 + tail.size();
    }

    @Override
    public Object head() {
        return head;
    }

    @Override
    public SList tail() {
        return tail;
    }

    @Override
    public <T> T foldl(T initial, BiFunction<T, Object, T> function) {
        return tail.foldl(function.apply(initial, head), function);
    }

    @Override
    public SList map(Function<Object, Object> function) {
        return new Cons(function.apply(head), tail.map(function));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Cons objects = (Cons) o;
        return head.equals(objects.head) && tail.equals(objects.tail);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + head.hashCode();
        result = 31 * result + tail.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("(");

        for (SList cur = this; cur != SList.nil; cur = cur.tail()) {
            builder.append(cur.head());
        }

        builder.append(')');

        return builder.toString();
    }
}
