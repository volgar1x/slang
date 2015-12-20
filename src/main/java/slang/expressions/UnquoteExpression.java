package slang.expressions;

/**
 * @author Antoine Chauvin
 */
public final class UnquoteExpression implements ExpressionInterface {
    private final String name;

    public UnquoteExpression(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <R> R visit(Visitor<R> visitor) {
        return visitor.visitUnquote(this);
    }

    @Override
    public String toString() {
        return "Unquote{" +
                "'" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnquoteExpression that = (UnquoteExpression) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
