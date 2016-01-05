package slang;

import java.util.function.BiFunction;

/**
 * @author Antoine Chauvin
 */
public final class Stdlib {
    public static void load(EvaluationContextInterface context) {
        IO.load(context);
        Core.load(context);
        Bool.load(context);
        Num.load(context);
        List.load(context);
        Code.load(context);
    }

    public static void loadFn(EvaluationContextInterface context, SName functionName, BiFunction<EvaluationContextInterface, SList, Object> pointer, boolean evaluateArguments) {
        context.register(functionName, new NativeFunction(functionName, evaluateArguments, pointer));
    }
}
