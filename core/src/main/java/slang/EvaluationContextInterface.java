package slang;

import java.util.function.UnaryOperator;

/**
 * @author Antoine Chauvin
 */
public interface EvaluationContextInterface extends UnaryOperator<Object> {
    EvaluationContextInterface linkTo(EvaluationContextInterface parent);
    EvaluationContextInterface link();
    Object evaluate(Object expression);

    Object read(SName identifier);
    boolean present(SName identifier);
    boolean hasOwn(SName identifier);
    void register(SName identifier, Object expression);

    ClassLoader getClassLoader();

    @Override
    default Object apply(Object expression) {
        return evaluate(expression);
    }
}
