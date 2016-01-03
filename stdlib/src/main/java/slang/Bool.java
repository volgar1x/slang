package slang;

import slang.visitors.Truth;

/**
 * @author Antoine Chauvin
 */
public final class Bool {
    public static void load(EvaluationContextInterface context) {
        Stdlib.loadFn(context, SAtom.of("="), Bool::eq, true);
        Stdlib.loadFn(context, SAtom.of("not"), Bool::not, true);
        Stdlib.loadFn(context, SAtom.of("<="), Bool::gte, true);
    }

    public static Object eq(EvaluationContextInterface context, SList arguments) {
        Object lhs = arguments.head();
        Object rhs = arguments.tail().head();

        if (lhs instanceof Number && rhs instanceof Number) {
            long l = ((Number) lhs).longValue();
            long r = ((Number) rhs).longValue();
            return l == r;
        }

        return lhs.equals(rhs);
    }

    public static Object not(EvaluationContextInterface context, SList arguments) {
        if (Truth.truthy(arguments.head())) {
            return SAtom.of("true");
        } else {
            return SList.nil;
        }
    }

    public static Object gte(EvaluationContextInterface context, SList arguments) {
        Object lhs = arguments.head();
        Object rhs = arguments.tail().head();

        if (!lhs.getClass().isAssignableFrom(rhs.getClass()) || !rhs.getClass().isAssignableFrom(lhs.getClass())) {
            throw new IllegalArgumentException(String.format("%s and %s are not comparable",
                    lhs.getClass().getName(), rhs.getClass().getName()));
        }

        if (!(lhs instanceof Comparable)) {
            throw new IllegalArgumentException(String.format("%s and %s are not comparable",
                    lhs.getClass().getName(), rhs.getClass().getName()));
        }

        //noinspection unchecked
        return ((Comparable) lhs).compareTo(rhs) >= 0 ? SAtom.of("true") : SList.nil;
    }
}
