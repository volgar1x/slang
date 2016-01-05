package slang;

import java.util.function.Function;

import static slang.visitors.Truth.truthy;

/**
 * @author Antoine Chauvin
 */
public final class SFn implements SFunction {

    private final SName functionName;
    private final SVector argumentNames;
    private final SList operations;

    public SFn(SName functionName, SVector argumentNames, SList operations) {
        this.functionName = functionName;
        this.argumentNames = argumentNames;
        this.operations = operations;
    }

    public static SFn fromList(SName functionName, SList list) {
        SVector argumentVector = (SVector) list.head();
        SList operations = list.tail();

        return new SFn(functionName, argumentVector, operations);
    }

    public static SFn fromList(SList list) {
        SName functionName = (SName) list.head();
        return fromList(functionName, list.tail());
    }

    @Override
    public SName getFunctionName() {
        return functionName;
    }

    @Override
    public boolean evaluateArguments() {
        return true;
    }

    @Override
    public Object call(EvaluationContextInterface context, SList arguments) {
        registerArguments(context, arguments);
        return operations.execute(context);
    }

    public SFn map(Function<SList, SList> function) {
        return new SFn(functionName, argumentNames, function.apply(operations));
    }

    private void registerArguments(EvaluationContextInterface context, SList arguments) {
        SList cur = arguments;
        for (Object arg : argumentNames) {
            SName argumentName = (SName) arg;
            if (argumentName.toString().equals("&")) {
                SName name = (SName) argumentNames.get(argumentNames.size() - 1);
                context.register(name, cur);
                break;
            }

            context.register(argumentName, cur.head());
            cur = cur.tail();
        }
    }

    public static SFunction tailCallOptimized(SFn function) {
        Object lastExpression = function.operations.last();
        if (!isFunctionCallTo(lastExpression, SName.of("cond"))) {
            return function;
        }
        SList last = (SList) lastExpression;

        // make sure (case) only have 2 choices
        if (last.size() != 3) {
            return function;
        }

        SList first = (SList) last.tail().head();
        SList second = (SList) last.tail().tail().head();

        final Object cond;
        final SList body, recur, result;

        if (isFunctionCallTo(first.last(), function.getFunctionName())) {
            cond = SList.of(SName.of("not"), first.head());
            body = first.tail().init();
            recur = (SList) first.tail().last();
            result = second.tail();
        } else if (isFunctionCallTo(second.last(), function.getFunctionName())) {
            cond = first.head();
            body = second.tail().init();
            recur = (SList) second.tail().last();
            result = first.tail();
        } else {
            return function;
        }

        return new NativeFunction(function.getFunctionName(), function.evaluateArguments(),
                (context, arguments) -> {
                    EvaluationContextInterface current = context.link();
                    function.registerArguments(current, arguments);

                    while (!truthy(current.evaluate(cond))) {
                        current.evaluate(body);

                        EvaluationContextInterface newCurrent = context.link();
                        function.registerArguments(newCurrent, recur.tail().map(current));
                        current = newCurrent;
                    }

                    return result.execute(current);
                });
    }

    private static boolean isFunctionCallTo(Object expression, SName functionName) {
        if (!(expression instanceof SList)) {
            return false;
        }

        SList list = (SList) expression;

        return list.head() instanceof SName && list.head().equals(functionName);
    }
}
