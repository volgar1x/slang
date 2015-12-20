package slang.expressions;

/**
 * @author Antoine Chauvin
 */
public final class NilExpression extends ListExpression {
    public static final NilExpression NIL = new NilExpression();

    private NilExpression() {

    }

    @Override
    public String toString() {
        return "nil";
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
