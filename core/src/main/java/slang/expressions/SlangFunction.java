package slang.expressions;

import java.util.List;

/**
 * @author Antoine Chauvin
 */
public class SlangFunction implements FunctionInterface {
    private final String functionName;
    private final List<String> argumentNames;
    private final ListExpression operations;

    public SlangFunction(String functionName, List<String> argumentNames, ListExpression operations) {
        this.functionName = functionName;
        this.argumentNames = argumentNames;
        this.operations = operations;
    }

    @Override
    public String getFunctionName() {
        return functionName;
    }

    @Override
    public ExpressionInterface call(EvaluationContextInterface context, ListExpression arguments) {
        final ListExpression[] holder = new ListExpression[]{arguments};
        argumentNames.forEach(argumentName -> {
            context.register(argumentName, holder[0].getHead());
            holder[0] = holder[0].getTail();
        });

        return operations.foldl(null, (operation, lastResult) -> context.evaluate(operation));
    }
}
