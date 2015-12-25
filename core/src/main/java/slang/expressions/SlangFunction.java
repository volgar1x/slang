package slang.expressions;

import java.util.ArrayList;
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

    public static SlangFunction fromList(ListExpression list) {
        String functionName = ((AtomExpression) list.getHead()).getAtom();
        VectorExpression argumentVector = (VectorExpression) list.getTail().getHead();
        ListExpression operations = list.getTail().getTail();

        List<String> argumentNames = new ArrayList<>(argumentVector.getLength());
        argumentVector.forEach(expression ->
                argumentNames.add(((AtomExpression) expression).getAtom()));

        return new SlangFunction(functionName, argumentNames, operations);
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