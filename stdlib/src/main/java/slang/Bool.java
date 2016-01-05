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
        Stdlib.loadFn(context, SAtom.of("nil?"), Bool::nilTest, true);
        Stdlib.loadFn(context, SAtom.of("many?"), Bool::manyTest, true);
        Stdlib.loadFn(context, SAtom.of("list?"), Bool::listTest, true);
        Stdlib.loadFn(context, SAtom.of("map?"), Bool::mapTest, true);
        Stdlib.loadFn(context, SAtom.of("set?"), Bool::setTest, true);
        Stdlib.loadFn(context, SAtom.of("vec?"), Bool::vecTest, true);
        Stdlib.loadFn(context, SAtom.of("int?"), Bool::intTest, true);
        Stdlib.loadFn(context, SAtom.of("dec?"), Bool::decTest, true);
        Stdlib.loadFn(context, SAtom.of("str?"), Bool::strTest, true);
        Stdlib.loadFn(context, SAtom.of("atom?"), Bool::atomTest, true);
        Stdlib.loadFn(context, SAtom.of("fn?"), Bool::fnTest, true);
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

    private static Object _bool(boolean b) {
        return b ? SAtom.of("true") : SList.nil;
    }

    public static Object nilTest(EvaluationContextInterface context, SList arguments) {
        return _bool(arguments.head() == SList.nil);
    }

    public static Object manyTest(EvaluationContextInterface context, SList arguments) {
        return _bool(arguments.head() instanceof SMany);
    }

    public static Object listTest(EvaluationContextInterface context, SList arguments) {
        return _bool(arguments.head() instanceof SList);
    }

    public static Object mapTest(EvaluationContextInterface context, SList arguments) {
        return _bool(arguments.head() instanceof SMap);
    }

    public static Object setTest(EvaluationContextInterface context, SList arguments) {
        return _bool(arguments.head() instanceof SSet);
    }

    public static Object vecTest(EvaluationContextInterface context, SList arguments) {
        return _bool(arguments.head() instanceof SVector);
    }

    public static Object intTest(EvaluationContextInterface context, SList arguments) {
        return _bool(arguments.head() instanceof Byte
                || arguments.head() instanceof Short
                || arguments.head() instanceof Integer
                || arguments.head() instanceof Long);
    }

    public static Object decTest(EvaluationContextInterface context, SList arguments) {
        return _bool(arguments.head() instanceof Float || arguments.head() instanceof Double);
    }

    public static Object strTest(EvaluationContextInterface context, SList arguments) {
        return _bool(arguments.head() instanceof String);
    }

    public static Object atomTest(EvaluationContextInterface context, SList arguments) {
        return _bool(arguments.head() instanceof SAtom);
    }

    public static Object fnTest(EvaluationContextInterface context, SList arguments) {
        return _bool(arguments.head() instanceof SFunction);
    }
}
