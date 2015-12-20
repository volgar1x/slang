package slang.interpreter;

import slang.expressions.EvaluationContext;
import slang.expressions.EvaluationContextInterface;
import slang.expressions.ExpressionInterface;
import slang.expressions.Visitor;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Antoine Chauvin
 */
public final class MacroExpander extends EvaluationContext implements Visitor<ExpressionInterface> {
    public MacroExpander(InputStream stdin, PrintStream stdout, PrintStream stderr) {
        super(stdin, stdout, stderr);
    }

    public MacroExpander(EvaluationContextInterface parent) {
        super(parent);
    }

    @Override
    public EvaluationContextInterface link() {
        return new MacroExpander(this);
    }

    @Override
    public ExpressionInterface apply(ExpressionInterface expression) {
        return evaluate(expression);
    }

    @Override
    public ExpressionInterface evaluate(ExpressionInterface expression) {
        return expression.visit(this);
    }

    @Override
    public ExpressionInterface otherwise(ExpressionInterface expression) {
        return expression;
    }


}
