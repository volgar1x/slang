package slang.expressions;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Antoine Chauvin
 */
public class LinkedEvaluationContext implements EvaluationContextInterface {
    private final EvaluationContextInterface superContext;
    private final Map<String, ExpressionInterface> expressions = new HashMap<>();

    public LinkedEvaluationContext(EvaluationContextInterface superContext) {
        this.superContext = superContext;
    }

    @Override
    public ExpressionInterface read(String identifier) {
        ExpressionInterface expression = expressions.get(identifier);
        if (expression == null) {
            return superContext.read(identifier);
        }
        return expression;
    }

    @Override
    public void register(String identifier, ExpressionInterface expression) {
        expressions.put(identifier, expression);
    }

    @Override
    public EvaluationInterface getEvaluation() {
        return superContext.getEvaluation();
    }

    @Override
    public InputStream getStandardInput() {
        return superContext.getStandardInput();
    }

    @Override
    public PrintStream getStandardOutput() {
        return superContext.getStandardOutput();
    }

    @Override
    public PrintStream getStandardError() {
        return superContext.getStandardError();
    }
}
