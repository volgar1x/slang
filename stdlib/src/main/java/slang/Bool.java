package slang;

/**
 * @author Antoine Chauvin
 */
public final class Bool {
    public static void load(EvaluationContextInterface context) {
        Stdlib.loadFn(context, SAtom.of("="), Bool::eq, true);
    }

    public static Object eq(EvaluationContextInterface context, SList arguments) {
        Object lhs = arguments.head();
        Object rhs = arguments.tail().head();

        if (lhs instanceof Comparable && rhs instanceof Comparable && lhs.getClass().isAssignableFrom(rhs.getClass())) {
            return ((Comparable) lhs).compareTo(rhs) == 0;
        }

        if (lhs instanceof Number && rhs instanceof Number) {
            long l = ((Number) lhs).longValue();
            long r = ((Number) rhs).longValue();
            return l == r;
        }

        return lhs.equals(rhs);
    }
}
