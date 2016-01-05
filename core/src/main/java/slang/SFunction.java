package slang;

/**
 * @author Antoine Chauvin
 */
@FunctionalInterface
public interface SFunction {

    Object call(EvaluationContextInterface context, SList arguments);

    default SName getFunctionName() {
        return SName.of("undefined");
    }

    default boolean evaluateArguments() {
        return false;
    }
}
