package slang.interpreter;

import slang.expressions.*;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Antoine Chauvin
 */
public final class Interpreter extends EvaluationContext implements Visitor<ExpressionInterface> {
    public Interpreter(InputStream stdin, PrintStream stdout, PrintStream stderr) {
        super(stdin, stdout, stderr);
        Stdlib.load(this);
    }

    public Interpreter(EvaluationContextInterface parent) {
        super(parent);
    }

    @Override
    public EvaluationContextInterface link() {
        return new Interpreter(this);
    }

    @Override
    public ExpressionInterface evaluate(ExpressionInterface expression) {
        return expression.visit(this);
    }

    @Override
    public ExpressionInterface apply(ExpressionInterface expression) {
        return expression.visit(this);
    }

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
        return read(atom.getAtom());
    }

    @Override
    public ExpressionInterface visitList(ListExpression list) {
        AtomExpression functionIdentifier = (AtomExpression) list.getHead();
        ListExpression functionArguments = list.getTail();

        FunctionInterface function = (FunctionInterface) read(functionIdentifier.getAtom());
        return function.call(this, functionArguments);
    }

    @Override
    public ExpressionInterface otherwise(ExpressionInterface expression) {
        return expression;
    }
}
