package slang;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Antoine Chauvin
 */
public interface SMany {
    <T> T foldl(T initial, BiFunction<T, Object, T> function);

    SMany map(Function<Object, Object> function);

    boolean isEmpty();

    interface Builder {
        void add(Object element);
        SMany build();
    }
}
