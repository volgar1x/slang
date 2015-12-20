package slang.expressions;

import slang.Pairs;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Antoine Chauvin
 */
public interface ManyExpressionInterface extends ExpressionInterface {
    interface Builder {
        void add(ExpressionInterface expression);
        ManyExpressionInterface build();
    }

    boolean isEmpty();

    <T> T foldl(T seed, BiFunction<ExpressionInterface, T, T> function);

    default void forEach(Consumer<ExpressionInterface> function) {
        foldl(null, (x, acc) -> {function.accept(x); return null;});
    }

    default String join(Function<ExpressionInterface, String> toString, String prefix, String between, String suffix) {
        String res = foldl(prefix, (x, acc) -> acc + toString.apply(x) + between);
        return res.substring(0, res.length() - between.length()) + suffix;
    }

    default Pairs<ExpressionInterface> pairs() {
        Pairs.Builder<ExpressionInterface> b = new Pairs.Builder<>();
        forEach(b::add);
        return b.build();
    }
}
