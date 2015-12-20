package slang.expressions;

/**
 * @author Antoine Chauvin
 */
@FunctionalInterface
public interface FunctionInterface extends ExpressionInterface {
    ExpressionInterface call(EvaluationContextInterface context, ListExpression arguments);

    default String getFunctionName() {
        return "undefined";
    }

    @Override
    default <R> R visit(Visitor<R> visitor) {
        return visitor.visitFunction(this);
    }
}
