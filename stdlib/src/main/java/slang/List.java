package slang;

/**
 * @author Antoine Chauvin
 */
public final class List {
    public static void load(EvaluationContextInterface context) {
        Stdlib.loadFn(context, SName.of("car"), List::car, true);
        Stdlib.loadFn(context, SName.of("cdr"), List::cdr, true);
        Stdlib.loadFn(context, SName.of("cons"), List::cons, true);
    }

    public static Object car(EvaluationContextInterface context, SList arguments) {
        return ((SList) arguments.head()).head();
    }

    public static Object cdr(EvaluationContextInterface context, SList arguments) {
        return ((SList) arguments.head()).tail();
    }

    public static Object cons(EvaluationContextInterface context, SList arguments) {
        return ((SList) arguments.tail().head()).push(arguments.head());
    }
}
