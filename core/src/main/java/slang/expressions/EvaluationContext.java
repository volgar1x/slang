package slang.expressions;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Antoine Chauvin
 */
public abstract class EvaluationContext implements EvaluationContextInterface {
    private final Map<String, ExpressionInterface> expressions = new HashMap<>();
    private final EvaluationContextInterface parent;
    private final InputStream stdin;
    private final PrintStream stdout, stderr;

    public EvaluationContext(InputStream stdin, PrintStream stdout, PrintStream stderr) {
        this.parent = null;
        this.stdin = stdin;
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public EvaluationContext(EvaluationContextInterface parent) {
        this.parent = parent;
        this.stdin = null;
        this.stdout = null;
        this.stderr = null;
    }

    @Override
    public ExpressionInterface read(String identifier) {
        ExpressionInterface result = expressions.get(identifier);
        if (result == null) {
            if (parent == null) {
                throw new SlangException(String.format("undefined variable `%s'", identifier));
            }
            return parent.read(identifier);
        }
        return result;
    }

    @Override
    public ExpressionInterface readMaybe(String identifier) {
        ExpressionInterface result = expressions.get(identifier);
        if (result == null && parent != null) {
            return parent.readMaybe(identifier);
        }
        return result;
    }

    @Override
    public void register(String identifier, ExpressionInterface expression) {
        expressions.put(identifier, expression);
    }

    @Override
    public InputStream getStandardInput() {
        return parent != null ? parent.getStandardInput() : stdin;
    }

    @Override
    public PrintStream getStandardOutput() {
        return parent != null ? parent.getStandardOutput() : stdout;
    }

    @Override
    public PrintStream getStandardError() {
        return parent != null ? parent.getStandardError() : stderr;
    }
}
