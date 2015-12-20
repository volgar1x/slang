package slang.expressions;

/**
 * @author Antoine Chauvin
 */
public interface ManyExpressionInterface extends ExpressionInterface {
    interface Builder {
        void add(ExpressionInterface expression);
        ManyExpressionInterface build();
    }
}
