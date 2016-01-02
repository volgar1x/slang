package slang.parser;

import slang.EvaluationContext;
import slang.EvaluationContextInterface;
import slang.Visitor;

/**
 * @author Antoine Chauvin
 */
public final class MacroExpander extends EvaluationContext implements Visitor<Object> {
    public MacroExpander(EvaluationContextInterface parent) {
        super(parent);
    }

    @Override
    public EvaluationContextInterface link() {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Object evaluate(Object expression) {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Object apply(Object expression) {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Object otherwise(Object expression) {
        throw new UnsupportedOperationException("todo");
    }
}
