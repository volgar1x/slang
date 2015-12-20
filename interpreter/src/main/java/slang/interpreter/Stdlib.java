package slang.interpreter;

import slang.Pairs;
import slang.expressions.*;
import slang.expressions.visitors.Printer;
import slang.expressions.visitors.Truthy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Antoine Chauvin
 */
public final class Stdlib {
    private Stdlib() {}

    public static void load(Interpreter interpreter) {
        interpreter.register("print", eval(Stdlib::print));
        interpreter.register("println", eval(Stdlib::println));
        interpreter.register("readln", eval(Stdlib::readln));
        interpreter.register("let", uneval(Stdlib::let));
        interpreter.register("def", uneval(Stdlib::def));
        interpreter.register("!", eval(Stdlib::not));
        interpreter.register("=", eval(Stdlib::equals));
        interpreter.register("!=", eval(Stdlib::nequals));
        interpreter.register("+", eval(Stdlib::plus));
        interpreter.register("-", eval(Stdlib::minus));
        interpreter.register("*", eval(Stdlib::times));
        interpreter.register("**", eval(Stdlib::pow));
        interpreter.register("/", eval(Stdlib::div));
        interpreter.register("%", eval(Stdlib::rem));
        interpreter.register("sqrt", eval(Stdlib::sqrt));
//        interpreter.register("cos", eval(Stdlib::cos));
//        interpreter.register("sin", eval(Stdlib::sin));
//        interpreter.register("tan", eval(Stdlib::tan));
    }
    
    private static FunctionInterface uneval(FunctionInterface function) {
        return function;
    }
    
    private static FunctionInterface eval(FunctionInterface function) {
        return (context, arguments) -> function.call(context, arguments.map(context));
    }

    public static ExpressionInterface print(EvaluationContextInterface context, ListExpression list) {
        context.getStandardOutput().print(list.map(context).join(Printer.INSTANCE, "", "", ""));
        return NilExpression.NIL;
    }

    public static ExpressionInterface println(EvaluationContextInterface context, ListExpression list) {
        context.getStandardOutput().print(list.map(context).join(Printer.INSTANCE, "", "", "\n"));
        return NilExpression.NIL;
    }

    public static ExpressionInterface readln(EvaluationContextInterface context, ListExpression list) {
        print(context, list);

        try {
            BufferedReader stdin = new BufferedReader(new InputStreamReader(context.getStandardInput()));
            String line = stdin.readLine();
            return new StringExpression(line);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static ExpressionInterface let(EvaluationContextInterface superContext, ListExpression list) {
        EvaluationContextInterface context = superContext.link();

        VectorExpression declarations = (VectorExpression) list.getHead();
        ListExpression operations = list.getTail();

        for (Pairs.Pair<ExpressionInterface> pair : declarations.pairs()) {
            context.register(((AtomExpression) pair.first).getAtom(), superContext.evaluate(pair.second));
        }

        return operations.<ExpressionInterface>foldl(NilExpression.NIL,
                (expression, lastResult) -> context.evaluate(expression));
    }

    public static ExpressionInterface def(EvaluationContextInterface parentContext, ListExpression list) {
        String functionName = ((AtomExpression) list.getHead()).getAtom();
        VectorExpression argumentNames = (VectorExpression) list.getTail().getHead();
        ListExpression operations = list.getTail().getTail();

        parentContext.register(functionName, new FunctionInterface() {
            @Override
            public String getFunctionName() {
                return functionName;
            }

            @Override
            public ExpressionInterface call(EvaluationContextInterface superContext, ListExpression arguments) {
                EvaluationContextInterface functionContext = superContext.link();

                final ListExpression[] holder = new ListExpression[]{arguments};
                argumentNames.forEach(x -> {
                    ListExpression cur = holder[0];

                    String argumentName = ((AtomExpression) x).getAtom();
                    ExpressionInterface argument = superContext.evaluate(cur.getHead());
                    functionContext.register(argumentName, argument);

                    holder[0] = cur.getTail();
                });

                return operations.foldl(null, (operation, lastResult) -> functionContext.evaluate(operation));
            }
        });

        return list.getHead();
    }

    public static ExpressionInterface not(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(Truthy.truthy(list.getHead()));
    }

    public static ExpressionInterface equals(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(list.getHead().equals(list.getTail().getHead()));
    }

    public static ExpressionInterface nequals(EvaluationContextInterface context, ListExpression list) {
        return BooleanExpression.from(!list.getHead().equals(list.getTail().getHead()));
    }

    public static NumExpression plus(EvaluationContextInterface context, ListExpression list) {
        return list.<NumExpression>foldl(IntegerExpression.ZERO, (x, acc) -> ((NumExpression) x).plus(acc));
    }

    public static NumExpression minus(EvaluationContextInterface context, ListExpression list) {
        NumExpression head = (NumExpression) list.getHead();
        if (list.getTail().isEmpty()) {
            return head;
        }
        return head.minus(plus(context, list.getTail()));
    }

    public static NumExpression times(EvaluationContextInterface context, ListExpression list) {
        return list.<NumExpression>foldl(IntegerExpression.ONE, (x, acc) -> ((NumExpression) x).times(acc));
    }

    public static NumExpression div(EvaluationContextInterface context, ListExpression list) {
        NumExpression head = (NumExpression) list.getHead();
        if (list.getTail().isEmpty()) {
            return head;
        }
        return head.div(times(context, list.getTail()));
    }

    public static NumExpression pow(EvaluationContextInterface context, ListExpression list) {
        NumExpression lhs = (NumExpression) list.getHead();
        NumExpression rhs = (NumExpression) list.getTail().getHead();
        return lhs.pow(rhs);
    }

    public static IntegerExpression rem(EvaluationContextInterface context, ListExpression list) {
        IntegerExpression lhs = (IntegerExpression) list.getHead();
        IntegerExpression rhs = (IntegerExpression) list.getTail().getHead();
        return new IntegerExpression(lhs.asInteger().remainder(rhs.asInteger()));
    }

    public static DecimalExpression sqrt(EvaluationContextInterface context, ListExpression list) {
        DecimalExpression lhs = (DecimalExpression) list.getHead();
        return lhs.sqrt();
    }
}
