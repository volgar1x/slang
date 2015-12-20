package slang.expressions;

/**
 * @author Antoine Chauvin
 */
public final class StringExpression implements ExpressionInterface {
    private final String string;

    public StringExpression(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return "String{" +
                "'" + string + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringExpression that = (StringExpression) o;

        return string.equals(that.string);

    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }
}
