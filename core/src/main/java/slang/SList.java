package slang;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Antoine Chauvin
 */
public interface SList extends List<Object>, SMany {
    SList nil = Nil.NIL;


    Object head();
    SList tail();

    Object fold(BiFunction<Object, Object, Object> function);

    @Override
    SList map(Function<Object, Object> function);

    default SList push(Object element) {
        return new Cons(element, this);
    }

    default Object last() {
        return this.<Object>foldl(nil, (acc, x) -> x);
    }

    default SList init() {
        SList acc = nil;
        SList cur = this;
        while (cur.tail() != nil) {
            acc = acc.push(cur.head());
        }
        return acc.reverse();
    }

    default SList reverse() {
        return foldl(nil, (acc, x) -> new Cons(x, acc));
    }

    default Object execute(Function<Object, Object> function) {
        return this.<Object>foldl(nil, (acc, x) -> function.apply(x));
    }

    default Iterator<Object> iterator() {
        return new Iterator<Object>() {
            SList cur = SList.this;

            @Override
            public boolean hasNext() {
                return cur != nil;
            }

            @Override
            public Object next() {
                Object head = cur.head();
                cur = cur.tail();
                return head;
            }
        };
    }

    static SList of(Object... elements) {
        SList tail = nil;
        for (int i = elements.length - 1; i >= 0; i--) {
            Object element = elements[i];
            tail = new Cons(element, tail);
        }
        return tail;
    }

    static SMany.Builder builder() {
        return new Builder();
    }

    class Builder implements SMany.Builder {

        SList cur = nil;

        @Override
        public void add(Object element) {
            cur = cur.push(element);
        }

        @Override
        public SList build() {
            return cur.reverse();
        }
    }
}
