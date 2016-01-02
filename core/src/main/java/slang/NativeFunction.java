package slang;

import java.util.function.BiFunction;

/**
 * @author Antoine Chauvin
 */
public class NativeFunction implements SFunction {
    private final SAtom functionName;
    private final BiFunction<EvaluationContextInterface, SList, Object> pointer;

    public NativeFunction(SAtom functionName, BiFunction<EvaluationContextInterface, SList, Object> pointer) {
        this.functionName = functionName;
        this.pointer = pointer;
    }

    @Override
    public SAtom getFunctionName() {
        return functionName;
    }

    @Override
    public Object call(EvaluationContextInterface context, SList arguments) {
        return pointer.apply(context, arguments);
    }
}
