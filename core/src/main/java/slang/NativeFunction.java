package slang;

import java.util.function.BiFunction;

/**
 * @author Antoine Chauvin
 */
public final class NativeFunction implements SFunction {
    private final SName functionName;
    private final BiFunction<EvaluationContextInterface, SList, Object> pointer;
    private final boolean evaluateArguments;

    public NativeFunction(SName functionName, boolean evaluateArguments, BiFunction<EvaluationContextInterface, SList, Object> pointer) {
        this.functionName = functionName;
        this.evaluateArguments = evaluateArguments;
        this.pointer = pointer;
    }

    @Override
    public SName getFunctionName() {
        return functionName;
    }

    @Override
    public Object call(EvaluationContextInterface context, SList arguments) {
        return pointer.apply(context, arguments);
    }

    @Override
    public boolean evaluateArguments() {
        return evaluateArguments;
    }
}
