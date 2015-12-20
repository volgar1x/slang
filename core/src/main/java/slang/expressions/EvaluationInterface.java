package slang.expressions;

/**
 * @author Antoine Chauvin
 */
public interface EvaluationInterface {
    ExpressionInterface evaluate(EvaluationContextInterface context, ExpressionInterface expression);
}
