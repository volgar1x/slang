package slang.expressions;

import java.util.ArrayList;
import java.util.List;

import static slang.expressions.Expressions.atom;
import static slang.expressions.Expressions.list;
import static slang.expressions.visitors.Truthy.truthy;

/**
 * @author Antoine Chauvin
 */
public class SlangFunction implements FunctionInterface {

    private final String functionName;
    private final List<String> argumentNames;
    private final ListExpression operations;
    private final Definition definition;

    public SlangFunction(String functionName, List<String> argumentNames, ListExpression operations, Definition definition) {
        this.functionName = functionName;
        this.argumentNames = argumentNames;
        this.operations = operations;
        this.definition = definition;
    }

    public static SlangFunction fromList(ListExpression list, Definition definition) {
        String functionName = ((AtomExpression) list.getHead()).getAtom();
        VectorExpression argumentVector = (VectorExpression) list.getTail().getHead();
        ListExpression operations = list.getTail().getTail();

        List<String> argumentNames = new ArrayList<>(argumentVector.getLength());
        argumentVector.forEach(expression ->
                argumentNames.add(((AtomExpression) expression).getAtom()));

        return new SlangFunction(functionName, argumentNames, operations, definition);
    }

    public static SlangFunction fromList(ListExpression list) {
        return fromList(list, Definition.RUN_TIME);
    }

    @Override
    public String getFunctionName() {
        return functionName;
    }

    public Definition getDefinition() {
        return definition;
    }

    @Override
    public ExpressionInterface call(EvaluationContextInterface context, ListExpression arguments) {
        registerArguments(context, arguments);
        return operations.foldl(null, (operation, lastResult) -> context.evaluate(operation));
    }

    private void registerArguments(EvaluationContextInterface context, ListExpression arguments) {
        ListExpression cur = arguments;
        for (String argumentName : argumentNames) {
            if (argumentName.equals("&")) {
                String name = argumentNames.get(argumentNames.size() - 1);
                context.register(name, cur);
                break;
            }

            context.register(argumentName, cur.getHead());
            cur = cur.getTail();
        }
    }

    public static FunctionInterface tailCallOptimized(SlangFunction function) {
        ExpressionInterface lastExpression = last(function.operations);
        if (!isFunctionCallTo(lastExpression, "case")) {
            return function;
        }
        ListExpression last = (ListExpression) lastExpression;

        // make sure (case) only have 2 choices
        if (last.length() != 3) {
            return function;
        }

        ListExpression first = (ListExpression) last.getTail().getHead();
        ListExpression second = (ListExpression) last.getTail().getTail().getHead();

        final ExpressionInterface cond;
        final ListExpression body, recur, result;

        if (isFunctionCallTo(last(first), function.getFunctionName())) {
            cond = list(atom("not"), first.getHead());
            body = initial(first.getTail());
            recur = (ListExpression) last(first.getTail());
            result = second.getTail();
        } else if (isFunctionCallTo(last(second), function.getFunctionName())) {
            cond = first.getHead();
            body = initial(second.getTail());
            recur = (ListExpression) last(second.getTail());
            result = first.getTail();
        } else {
            return function;
        }

        return new NativeFunction(function.getFunctionName(), Definition.RUN_TIME,
                (context, arguments) -> {
                    EvaluationContextInterface current = context.link();
                    function.registerArguments(current, arguments.map(current));

                    while (!truthy(current.evaluate(cond))) {
                        current.evaluate(body);

                        EvaluationContextInterface newCurrent = context.link();
                        function.registerArguments(newCurrent, recur.getTail().map(current));
                        current = newCurrent;
                    }

                    return last(result.map(current));
                });
    }

    private static ExpressionInterface last(ListExpression list) {
        if (list.getTail().isEmpty()) {
            return list.getHead();
        }
        return last(list.getTail());
    }

    private static ListExpression initial(ListExpression list) {
        ListExpression.Builder builder = new ListExpression.Builder();

        ListExpression cur = list;
        while (!cur.getTail().isEmpty()) {
            builder.add(cur.getHead());
            cur = cur.getTail();
        }

        return builder.build();
    }

    private static boolean isFunctionCallTo(ExpressionInterface expression, String functionName) {
        if (!(expression instanceof ListExpression)) {
            return false;
        }

        ListExpression list = (ListExpression) expression;

        return list.getHead() instanceof AtomExpression &&
                ((AtomExpression) list.getHead()).getAtom().equals(functionName);
    }
}
