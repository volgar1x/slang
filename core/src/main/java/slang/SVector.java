package slang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * @author Antoine Chauvin
 */
public final class SVector extends ArrayList<Object> implements SMany {

    @Override
    public <T> T foldl(T initial, BiFunction<T, Object, T> function) {
        T acc = initial;

        for (Object element : this) {
            acc = function.apply(acc, element);
        }

        return acc;
    }

    @Override
    public SVector map(Function<Object, Object> function) {
        Builder builder = new Builder();

        for (Object element : this) {
            builder.add(function.apply(element));
        }

        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static SVector of(Object... elements) {
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
    public Object set(int index, Object element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, Object element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(int index) {
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
    public boolean addAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
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

    @Override
    public void replaceAll(UnaryOperator<Object> operator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sort(Comparator<? super Object> c) {
        throw new UnsupportedOperationException();
    }

    public static class Builder implements SMany.Builder {
        SVector cur = new SVector();

        @Override
        public void add(Object element) {
            cur._add(element);
        }

        @Override
        public SVector build() {
            return cur;
        }
    }
}
