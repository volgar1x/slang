package slang.expressions;

/**
 * @author Antoine Chauvin
 */
public final class AtomExpression implements ExpressionInterface {
    public static AtomExpression T = new AtomExpression("t");

    private final String atom;

    public AtomExpression(String atom) {
        this.atom = atom;
    }

    public String getAtom() {
        return atom;
    }

    @Override
    public <R> R visit(Visitor<R> visitor) {
        return visitor.visitAtom(this);
    }

    @Override
    public String toString() {
        return "Atom{" +
                "'" + atom + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AtomExpression that = (AtomExpression) o;

        return atom.equals(that.atom);

    }

    @Override
    public int hashCode() {
        return atom.hashCode();
    }
}
