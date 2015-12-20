package slang;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Antoine Chauvin
 */
public final class Pairs<T> implements Iterable<Pairs.Pair<T>> {
    public static class Pair<T> {
        public final T first, second;

        public Pair(T first, T second) {
            this.first = first;
            this.second = second;
        }
    }

    private final Pair<T>[] pairs;

    private Pairs(Pair<T>[] pairs) {
        this.pairs = pairs;
    }

    @Override
    public Iterator<Pair<T>> iterator() {
        return new Iterator<Pair<T>>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < pairs.length;
            }

            @Override
            public Pair<T> next() {
                return pairs[index++];
            }
        };
    }

    public static class Builder<T> {
        private final List<T> list = new LinkedList<>();

        public void add(T element) {
            list.add(element);
        }

        public Pairs<T> build() {
            if (list.size() % 2 != 0) {
                throw new IllegalStateException("cannot build a list of pair from an odd sequence");
            }

            @SuppressWarnings("unchecked")
            Pair<T>[] pairs = new Pair[list.size() / 2];

            for (int i = 0; i < pairs.length; i++) {
                pairs[i] = new Pair<>(list.get(i*2), list.get(i*2+1));
            }

            return new Pairs<>(pairs);
        }
    }
}
