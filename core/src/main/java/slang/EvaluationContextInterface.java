package slang;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.function.UnaryOperator;

/**
 * @author Antoine Chauvin
 */
public interface EvaluationContextInterface extends UnaryOperator<Object> {
    EvaluationContextInterface link();
    Object evaluate(Object expression);

    Object read(SName identifier);
    boolean present(SName identifier);
    boolean hasOwn(SName identifier);
    void register(SName identifier, Object expression);

    ClassLoader getClassLoader();
    InputStream getStandardInput();
    PrintStream getStandardOutput();
    PrintStream getStandardError();

    @Override
    default Object apply(Object expression) {
        return evaluate(expression);
    }
}
