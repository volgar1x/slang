package slang.expressions;

import java.util.function.BiFunction;

/**
 * @author Antoine Chauvin
 */
public class NativeFunction implements FunctionInterface {
    private final String functionName;
    private final Definition definition;
    private final BiFunction<EvaluationContextInterface, ListExpression, ExpressionInterface> pointer;

    public NativeFunction(String functionName, Definition definition, BiFunction<EvaluationContextInterface, ListExpression, ExpressionInterface> pointer) {
        this.functionName = functionName;
        this.definition = definition;
        this.pointer = pointer;
    }

    @Override
    public String getFunctionName() {
        return functionName;
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    @Override
    public ExpressionInterface call(EvaluationContextInterface context, ListExpression arguments) {
        return pointer.apply(context, arguments);
    }
}
