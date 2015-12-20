package slang.interpreter;

import slang.expressions.*;
import slang.expressions.visitors.BaseVisitor;

/**
 * @author Antoine Chauvin
 */
public enum InterpreterEvaluation implements EvaluationInterface {
    INSTANCE;

    @Override
    public ExpressionInterface evaluate(EvaluationContextInterface context, ExpressionInterface expression) {
        return expression.visit(new BaseVisitor<ExpressionInterface>() {
            @Override
            public ExpressionInterface visitQuote(QuoteExpression quote) {
                return quote.getExpression();
            }

            @Override
            public ExpressionInterface visitUnquote(UnquoteExpression unquote) {
                throw new IllegalStateException();
            }

            @Override
            public ExpressionInterface visitAtom(AtomExpression atom) {
                return context.read(atom.getAtom());
            }

            @Override
            public ExpressionInterface visitList(ListExpression list) {
                AtomExpression functionIdentifier = (AtomExpression) list.getHead();
                ListExpression functionArguments = list.getTail();

                FunctionInterface function = (FunctionInterface) context.read(functionIdentifier.getAtom());
                return function.call(context, functionArguments);
            }

            @Override
            public ExpressionInterface otherwise(ExpressionInterface expression) {
                return expression;
            }
        });
    }
}
