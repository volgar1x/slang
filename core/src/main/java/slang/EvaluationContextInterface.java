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

    Object read(SAtom identifier);
    boolean present(SAtom identifier);
    void register(SAtom identifier, Object expression);

    ClassLoader getClassLoader();
    InputStream getStandardInput();
    PrintStream getStandardOutput();
    PrintStream getStandardError();

    @Override
    default Object apply(Object expression) {
        return evaluate(expression);
    }
}
