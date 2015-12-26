package slang;

import slang.expressions.*;
import slang.expressions.visitors.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Antoine Chauvin
 */
class IO {

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
}
