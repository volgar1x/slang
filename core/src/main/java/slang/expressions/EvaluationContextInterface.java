package slang.expressions;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.function.UnaryOperator;

/**
 * @author Antoine Chauvin
 */
public interface EvaluationContextInterface extends UnaryOperator<ExpressionInterface> {
    EvaluationContextInterface link();
    ExpressionInterface evaluate(ExpressionInterface expression);

    ExpressionInterface read(String identifier);
    /** @nullable */
    ExpressionInterface readMaybe(String identifier);
    void register(String identifier, ExpressionInterface expression);

    InputStream getStandardInput();
    PrintStream getStandardOutput();
    PrintStream getStandardError();

    @Override
    default ExpressionInterface apply(ExpressionInterface expression) {
        return evaluate(expression);
    }
}
