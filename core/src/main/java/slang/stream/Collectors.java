package slang.stream;

import slang.SList;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * @author Antoine Chauvin
 */
public final class Collectors {

    private static final Collector<Object, SList.Builder, SList> SLIST = new Collector<Object, SList.Builder, SList>() {
        @Override
        public Supplier<SList.Builder> supplier() {
            return SList::builder;
        }

        @Override
        public BiConsumer<SList.Builder, Object> accumulator() {
            return SList.Builder::add;
        }

        @Override
        public BinaryOperator<SList.Builder> combiner() {
            return (left, right) -> {
                left.addAll(right);
                return left;
            };
        }

        @Override
        public Function<SList.Builder, SList> finisher() {
            return SList.Builder::build;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }
    };

    public static Collector<Object, ?, SList> list() {
        return SLIST;
    }
}
