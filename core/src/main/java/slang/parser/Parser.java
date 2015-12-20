package slang.parser;

import slang.expressions.*;
import slang.tokenizer.ConstToken;
import slang.tokenizer.Token;
import slang.tokenizer.TokenInterface;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Iterator;

/**
 * @author Antoine Chauvin
 */
public final class Parser implements Iterator<ExpressionInterface> {
    private final Iterator<TokenInterface> tokenizer;

    public Parser(Iterator<TokenInterface> tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public boolean hasNext() {
        return tokenizer.hasNext();
    }

    @Override
    public ExpressionInterface next() {
        return next(tokenizer.next());
    }

    private ExpressionInterface next(TokenInterface token) {
        if (token instanceof ConstToken) {
            switch ((ConstToken) token) {
                case START_LIST:
                    return nextMany(new ListExpression.Builder(), ConstToken.END_LIST);
                case START_SET:
                    return nextMany(new SetExpression.Builder(), ConstToken.END_SET);
                case START_VECTOR:
                    return nextMany(new VectorExpression.Builder(), ConstToken.END_VECTOR);

                case DOUBLE_QUOTE:
                    String string = nextTokenValue();
                    tokenizer.next().expect(ConstToken.DOUBLE_QUOTE);
                    return new StringExpression(string);

                case QUOTE:
                    return new QuoteExpression(next());

                case UNQUOTE:
                    return new UnquoteExpression(nextTokenValue());

                case EOF:
                    return NilExpression.NIL;
            }
        }

        return coerce(token.getValue());
    }

    private String nextTokenValue() {
        TokenInterface token = tokenizer.next();
        if (token instanceof Token) {
            return token.getValue();
        }
        throw new RuntimeException("unexpected token `" + token + "'");
    }

    private ManyExpressionInterface nextMany(ManyExpressionInterface.Builder builder, ConstToken delim) {
        while (true) {
            TokenInterface token = tokenizer.next();
            if (token.equals(delim)) {
                break;
            }
            ExpressionInterface expression = next(token);
            builder.add(expression);
        }
        return builder.build();
    }

    private ExpressionInterface coerce(String value) {
        if (isInteger(value)) {
            return new IntegerExpression(new BigInteger(value, 10));
        } else if (isDecimal(value)) {
            return new DecimalExpression(new BigDecimal(value, MathContext.UNLIMITED));
        }
        return new AtomExpression(value);
    }

    private boolean isInteger(String value) {
        for (int i = 0; i < value.length(); i++) {
            char chr = value.charAt(i);
            if (chr < '0' || chr > '9') {
                return false;
            }
        }
        return true;
    }

    private boolean isDecimal(String value) {
        int sep = value.indexOf('.');
        // must begin and end by a digit
        if (sep <= 0 || sep >= value.length()) {
            return false;
        }
        // only one decimal separator must be present
        if (value.indexOf('.', sep+1) > 0) {
            return false;
        }
        // the rest must be all digits
        return isInteger(value.replace(".", ""));
    }
}
