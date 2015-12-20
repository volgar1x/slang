package slang.interpreter;

import slang.expressions.*;

/**
 * @author Antoine Chauvin
 */
public enum InterpreterEvaluation implements EvaluationInterface {
    INSTANCE;

    @Override
    public ExpressionInterface evaluate(EvaluationContextInterface context, ExpressionInterface expression) {
        if (expression instanceof QuoteExpression) {
            return ((QuoteExpression) expression).getExpression();
        } else if (expression instanceof UnquoteExpression) {
            throw new IllegalStateException();
        } else if (expression instanceof AtomExpression) {
            return context.read(((AtomExpression) expression).getAtom());
        } else if (expression instanceof ListExpression) {
            return evaluateFunctionCall(context, (ListExpression) expression);
        } else {
            return expression;
        }
    }

    private ExpressionInterface evaluateFunctionCall(EvaluationContextInterface context, ListExpression list) {
        AtomExpression functionIdentifier = (AtomExpression) list.getHead();
        ListExpression functionArguments = list.getTail();

        FunctionInterface function = (FunctionInterface) context.read(functionIdentifier.getAtom());
        return function.call(context, functionArguments);
    }
}
