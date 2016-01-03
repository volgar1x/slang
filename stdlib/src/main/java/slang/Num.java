package slang;

import slang.visitors.Inspector;

/**
 * @author Antoine Chauvin
 */
public final class Num {
    public static void load(EvaluationContextInterface context) {
        Stdlib.loadFn(context, SAtom.of("+"), Num::plus, true);
        Stdlib.loadFn(context, SAtom.of("-"), Num::minus, true);
        Stdlib.loadFn(context, SAtom.of("*"), Num::mul, true);
        Stdlib.loadFn(context, SAtom.of("/"), Num::div, true);
        Stdlib.loadFn(context, SAtom.of("%"), Num::rem, true);
        Stdlib.loadFn(context, SAtom.of("sqrt"), Num::sqrt, true);
    }

    public static Object plus(EvaluationContextInterface context, SList arguments) {
        return arguments.fold((acc, element) -> {
            if (element instanceof Long) {
                if (acc instanceof Long) {
                    return (Long) acc + (Long) element;
                } else if (acc instanceof Double) {
                    return (Double) acc + (Long) element;
                }
            } else if (element instanceof Double) {
                if (acc instanceof Double) {
                    return (Double) acc + (Double) element;
                } else if (acc instanceof Long) {
                    return (Long) acc + (Double) element;
                }
            }

            throw new IllegalArgumentException(String.format("cannot add %s and %s",
                    Inspector.inspect(element),
                    Inspector.inspect(acc)));
        });
    }

    public static Object minus(EvaluationContextInterface context, SList arguments) {
        Object lhs = arguments.head();
        Object rhs = plus(context, arguments.tail());

        if (lhs instanceof Long) {
            if (rhs instanceof Long) {
                return (Long) lhs - (Long) rhs;
            } else if (rhs instanceof Double) {
                return (Long) lhs - (Double) rhs;
            }
        } else if (lhs instanceof Double) {
            if (rhs instanceof Double) {
                return (Double) lhs - (Double) rhs;
            } else if (rhs instanceof Long) {
                return (Double) lhs - (Long) rhs;
            }
        }

        throw new IllegalArgumentException(String.format("cannot substract %s and %s",
                Inspector.inspect(lhs),
                Inspector.inspect(arguments.tail().head())));
    }

    public static Object mul(EvaluationContextInterface context, SList arguments) {
        return arguments.fold((acc, element) -> {
            if (element instanceof Long) {
                if (acc instanceof Long) {
                    return (Long) acc * (Long) element;
                } else if (acc instanceof Double) {
                    return (Double) acc * (Long) element;
                }
            } else if (element instanceof Double) {
                if (acc instanceof Double) {
                    return (Double) acc * (Double) element;
                } else if (acc instanceof Long) {
                    return (Long) acc * (Double) element;
                }
            }

            throw new IllegalArgumentException(String.format("cannot multiply %s and %s",
                    Inspector.inspect(element),
                    Inspector.inspect(acc)));
        });
    }

    public static Object div(EvaluationContextInterface context, SList arguments) {
        Object lhs = arguments.head();
        Object rhs = mul(context, arguments.tail());

        if (lhs instanceof Number && rhs instanceof Number) {
            return ((Number) lhs).doubleValue() / ((Number) rhs).doubleValue();
        }

        throw new IllegalArgumentException(String.format("cannot divide %s by %s",
                Inspector.inspect(lhs),
                Inspector.inspect(arguments.tail().head())));
    }

    public static Object rem(EvaluationContextInterface context, SList arguments) {
        Object lhs = arguments.head();
        Object rhs = arguments.tail().head();

        if (lhs instanceof Long && rhs instanceof Long) {
            return ((Number) lhs).longValue() % ((Number) rhs).longValue();
        }

        throw new IllegalArgumentException(String.format("cannot compute remainder of euclidian division of %s by %s",
                Inspector.inspect(lhs),
                Inspector.inspect(arguments.tail().head())));
    }

    public static Object sqrt(EvaluationContextInterface context, SList arguments) {
        Object num = arguments.head();

        if (num instanceof Number) {
            return Math.sqrt(((Number) num).doubleValue());
        }

        throw new IllegalArgumentException(String.format("cannot compute square root of %s",
                Inspector.inspect(num)));
    }
}
