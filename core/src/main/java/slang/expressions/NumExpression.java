package slang.expressions;

import java.math.BigDecimal;

/**
 * @author Antoine Chauvin
 */
public abstract class NumExpression implements ExpressionInterface {
    public abstract BigDecimal asDecimal();

    public abstract NumExpression plus(NumExpression num);
    public abstract NumExpression minus(NumExpression num);
    public abstract NumExpression times(NumExpression num);
    public abstract NumExpression div(NumExpression num);
    public abstract NumExpression pow(NumExpression num);
}
