package slang.interpreter;

import slang.Pairs;
import slang.expressions.*;
import slang.expressions.visitors.Printer;

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
}
