package slang.interpreter;

import slang.*;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Antoine Chauvin
 */
public final class Interpreter extends EvaluationContext implements Visitor<Object> {
    public Interpreter(InputStream stdin, PrintStream stdout, PrintStream stderr) {
        super(stdin, stdout, stderr);
    }

    private Interpreter(EvaluationContextInterface parent) {
        super(parent);
    }

    @Override
    public EvaluationContextInterface link() {
        return new Interpreter(this);
    }

    @Override
    public Object evaluate(Object expression) {
        return Visitor.super.apply(expression);
    }

    @Override
    public Object apply(Object expression) {
        return Visitor.super.apply(expression);
    }

    @Override
    public Object otherwise(Object expression) {
        return expression;
    }

    @Override
    public Object visitQuote(SQuote quote) {
        return quote.quoted;
    }

    @Override
    public Object visitUnquote(SUnquote unquote) {
        throw new SExpression("tried to unquote while interpreting");
    }

    @Override
    public Object visitList(SList list) {
        throw new UnsupportedOperationException("todo");
    }
}
