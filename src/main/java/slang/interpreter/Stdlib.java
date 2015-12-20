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
        interpreter.register("print", (FunctionInterface) Stdlib::print);
        interpreter.register("println", (FunctionInterface) Stdlib::println);
        interpreter.register("readln", (FunctionInterface) Stdlib::readln);
        interpreter.register("let", (FunctionInterface) Stdlib::let);
        interpreter.register("!", (FunctionInterface) Stdlib::not);
        interpreter.register("=", (FunctionInterface) Stdlib::equals);
        interpreter.register("!=", (FunctionInterface) Stdlib::nequals);
        interpreter.register("+", (FunctionInterface) Stdlib::plus);
        interpreter.register("-", (FunctionInterface) Stdlib::minus);
        interpreter.register("*", (FunctionInterface) Stdlib::times);
        interpreter.register("**", (FunctionInterface) Stdlib::pow);
        interpreter.register("/", (FunctionInterface) Stdlib::div);
        interpreter.register("%", (FunctionInterface) Stdlib::rem);
        interpreter.register("sqrt", (FunctionInterface) Stdlib::sqrt);
//        interpreter.register("cos", (FunctionInterface) Stdlib::cos);
//        interpreter.register("sin", (FunctionInterface) Stdlib::sin);
//        interpreter.register("tan", (FunctionInterface) Stdlib::tan);
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
