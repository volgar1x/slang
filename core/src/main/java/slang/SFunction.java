package slang;

/**
 * @author Antoine Chauvin
 */
@FunctionalInterface
public interface SFunction {

    Object call(EvaluationContextInterface context, SList arguments);

    default SAtom getFunctionName() {
        return SAtom.of("undefined");
    }
}
