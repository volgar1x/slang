package slang;

import slang.expressions.*;
import slang.expressions.visitors.Inspector;
import slang.expressions.visitors.Printer;
import slang.expressions.visitors.Truthy;

/**
 * @author Antoine Chauvin
 */
class Core {
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

    public static ExpressionInterface case_(EvaluationContextInterface context, ListExpression list) {
        for (ListExpression rest = list; !rest.isEmpty(); rest = rest.getTail()) {
            ListExpression cur = (ListExpression) rest.getHead();

            ExpressionInterface condition = cur.getHead();
            ExpressionInterface operation = cur.getTail().getHead();

            if (Truthy.truthy(context.evaluate(condition))) {
                return context.evaluate(operation);
            }
        }

        return NilExpression.NIL;
    }

    public static ExpressionInterface def(EvaluationContextInterface parentContext, ListExpression list) {
        SlangFunction function = SlangFunction.fromList(list);
        parentContext.register(function.getFunctionName(), function);
        return function;
    }

    public static ExpressionInterface inspect(EvaluationContextInterface context, ListExpression list) {
        return new StringExpression(Inspector.inspect(list.getHead()));
    }

    public static NilExpression raise(EvaluationContextInterface context, ListExpression arguments) {
        throw new SlangException("error: " + Printer.print(arguments));
    }
}
