package slang.expressions;

/**
 * @author Antoine Chauvin
 */
public final class BooleanExpression {
    private BooleanExpression() {}

    public static ExpressionInterface from(boolean b) {
        return b ? AtomExpression.T : NilExpression.NIL;
    }
}
