package slang;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Antoine Chauvin
 */
public abstract class EvaluationContext implements EvaluationContextInterface {
    private final Map<SName, Object> expressions = new HashMap<>();
    private final EvaluationContextInterface parent;
    private final ClassLoader classLoader;

    public EvaluationContext(ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.parent = null;
    }

    public EvaluationContext(EvaluationContextInterface parent) {
        this.parent = parent;
        this.classLoader = null;
    }

    @Override
    public EvaluationContextInterface link() {
        return linkTo(this);
    }

    @Override
    public Object read(SName identifier) {
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
    public boolean present(SName identifier) {
        return expressions.containsKey(identifier) || parent != null && parent.present(identifier);

    }

    @Override
    public boolean hasOwn(SName identifier) {
        return expressions.containsKey(identifier);
    }

    @Override
    public void register(SName identifier, Object expression) {
        expressions.put(identifier, expression);
    }

    @Override
    public ClassLoader getClassLoader() {
        return parent != null ? parent.getClassLoader() : classLoader;
    }
}
