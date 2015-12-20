package slang.expressions;

/**
 * @author Antoine Chauvin
 */
public interface ExpressionInterface {
    <R> R visit(Visitor<R> visitor);
}
