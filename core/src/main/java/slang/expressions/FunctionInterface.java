package slang.expressions;

/**
 * @author Antoine Chauvin
 */
@FunctionalInterface
public interface FunctionInterface extends ExpressionInterface {
    enum Definition {
        COMPILE_TIME,
        RUN_TIME,
    }

    ExpressionInterface call(EvaluationContextInterface context, ListExpression arguments);

    default String getFunctionName() {
        return "undefined";
    }
    default Definition getDefinition() { return Definition.RUN_TIME; }

    @Override
    default <R> R visit(Visitor<R> visitor) {
        return visitor.visitFunction(this);
    }
}
