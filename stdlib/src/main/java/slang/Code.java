package slang;

/**
 * @author Antoine Chauvin
 */
public final class Code {
    public static void load(EvaluationContextInterface context) {
        Stdlib.loadFn(context, SAtom.of("eval"), Code::eval, true);
    }

    public static Object eval(EvaluationContextInterface context, SList arguments) {
        return arguments.execute(context);
    }
}
