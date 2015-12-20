package slang.expressions;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Antoine Chauvin
 */
public class RootEvaluationContext implements EvaluationContextInterface {
    private final Map<String, ExpressionInterface> expressions = new HashMap<>();
    private final EvaluationInterface evaluation;
    private final InputStream stdin;
    private final PrintStream stdout, stderr;

    public RootEvaluationContext(EvaluationInterface evaluation, InputStream stdin, PrintStream stdout, PrintStream stderr) {
        this.evaluation = evaluation;
        this.stdin = stdin;
        this.stdout = stdout;
        this.stderr = stderr;
    }

    @Override
    public EvaluationInterface getEvaluation() {
        return evaluation;
    }

    @Override
    public ExpressionInterface read(String identifier) {
        ExpressionInterface result = expressions.get(identifier);
        if (result == null) {
            throw new NoSuchElementException(identifier);
        }
        return result;
    }

    @Override
    public ExpressionInterface readMaybe(String identifier) {
        return expressions.get(identifier);
    }

    @Override
    public void register(String identifier, ExpressionInterface expression) {
        expressions.put(identifier, expression);
    }

    @Override
    public InputStream getStandardInput() {
        return stdin;
    }

    @Override
    public PrintStream getStandardOutput() {
        return stdout;
    }

    @Override
    public PrintStream getStandardError() {
        return stderr;
    }
}
