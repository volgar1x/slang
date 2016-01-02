package slang;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Antoine Chauvin
 */
public final class SSet extends HashSet<Object> implements SMany {

    @Override
    public <T> T foldl(T initial, BiFunction<T, Object, T> function) {
        T acc = initial;

        for (Object element : this) {
            acc = function.apply(acc, element);
        }

        return acc;
    }

    @Override
    public SSet map(Function<Object, Object> function) {
        Builder builder = new Builder();

        for (Object element : this) {
            builder.add(function.apply(element));
        }

        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static SSet of(Object... elements) {
        Builder builder = new Builder();

        for (Object element : elements) {
            builder.add(element);
        }

        return builder.build();
    }

    // until we have a real persistent data structure
    private void _add(Object element) {
        super.add(element);
    }

    @Override
    public boolean add(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(Predicate<? super Object> filter) {
        throw new UnsupportedOperationException();
    }

    public static class Builder implements SMany.Builder {
        SSet cur = new SSet();

        @Override
        public void add(Object element) {
            cur._add(element);
        }

        @Override
        public SSet build() {
            return cur;
        }
    }
}
