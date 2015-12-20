package slang.expressions;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.function.UnaryOperator;

/**
 * @author Antoine Chauvin
 */
public interface EvaluationContextInterface extends UnaryOperator<ExpressionInterface> {
    EvaluationInterface getEvaluation();

    ExpressionInterface read(String identifier);
    void register(String identifier, ExpressionInterface expression);

    InputStream getStandardInput();
    PrintStream getStandardOutput();
    PrintStream getStandardError();

    default ExpressionInterface evaluate(ExpressionInterface expression) {
        return getEvaluation().evaluate(this, expression);
    }

    @Override
    default ExpressionInterface apply(ExpressionInterface expression) {
        return evaluate(expression);
    }

    default EvaluationContextInterface link() {
        return new LinkedEvaluationContext(this);
    }
}
