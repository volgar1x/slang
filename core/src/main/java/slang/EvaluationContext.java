package slang;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Antoine Chauvin
 */
public abstract class EvaluationContext implements EvaluationContextInterface {
    private final Map<SAtom, Object> expressions = new HashMap<>();
    private final EvaluationContextInterface parent;
    private final ClassLoader classLoader;
    private final InputStream stdin;
    private final PrintStream stdout, stderr;

    public EvaluationContext(ClassLoader classLoader, InputStream stdin, PrintStream stdout, PrintStream stderr) {
        this.classLoader = classLoader;
        this.parent = null;
        this.stdin = stdin;
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public EvaluationContext(EvaluationContextInterface parent) {
        this.parent = parent;
        this.classLoader = null;
        this.stdin = null;
        this.stdout = null;
        this.stderr = null;
    }

    protected EvaluationContextInterface getParent() {
        return parent;
    }

    @Override
    public Object read(SAtom identifier) {
        Object result = expressions.get(identifier);
        if (result == null) {
            if (parent == null) {
                throw new SException(String.format("undefined variable `%s'", identifier));
            }
            return parent.read(identifier);
        }
        return result;
    }

    @Override
    public boolean present(SAtom identifier) {
        return expressions.containsKey(identifier) || parent != null && parent.present(identifier);

    }

    @Override
    public boolean hasOwn(SAtom identifier) {
        return expressions.containsKey(identifier);
    }

    @Override
    public void register(SAtom identifier, Object expression) {
        expressions.put(identifier, expression);
    }

    @Override
    public ClassLoader getClassLoader() {
        return parent != null ? parent.getClassLoader() : classLoader;
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
