package slang.parser;

import slang.*;
import slang.tokenizer.ConstToken;
import slang.tokenizer.Token;
import slang.tokenizer.TokenInterface;

import java.util.Iterator;


/**
 * @author Antoine Chauvin
 */
public final class Parser implements Iterator<Object> {
    private final Iterator<TokenInterface> tokenizer;

    public Parser(Iterator<TokenInterface> tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public boolean hasNext() {
        return tokenizer.hasNext();
    }

    @Override
    public Object next() {
        return next(tokenizer.next());
    }

    private Object next(TokenInterface token) {
        if (token instanceof ConstToken) {
            switch ((ConstToken) token) {
                case START_LIST:
                    return nextMany(SList.builder(), ConstToken.END_LIST);
                case START_MAP:
                    return nextMap(SHashMap.builder(), ConstToken.END_MAP);
                case START_SET:
                    return nextMany(SSet.builder(), ConstToken.END_SET);
                case START_VECTOR:
                    return nextMany(SVector.builder(), ConstToken.END_VECTOR);

                case DOUBLE_QUOTE:
                    String string = nextTokenValue();
                    tokenizer.next().expect(ConstToken.DOUBLE_QUOTE);
                    return string;

                case QUOTE:
                    return new SQuote(next());

                case UNQUOTE:
                    return new SUnquote(nextTokenValue());

                case EOF:
                    return null;
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

    private SMany nextMany(SMany.Builder builder, ConstToken delim) {
        while (true) {
            TokenInterface token = tokenizer.next();
            if (token.equals(delim)) {
                break;
            }
            Object expression = next(token);
            builder.add(expression);
        }
        return builder.build();
    }

    private SMap nextMap(SMap.Builder builder, ConstToken delim) {
        TokenInterface token;
        while (true) {
            token = tokenizer.next();
            if (token.equals(delim)) {
                break;
            }
            Object key = next(token);
            Object value = next();
            builder.add(key, value);
        }
        return builder.build();
    }

    private Object coerce(String value) {
        if (isInteger(value)) {
            return Long.parseLong(value);
        }

        if (isDecimal(value)) {
            return Double.parseDouble(value);
        }

        if (value.charAt(0) == ':') {
            return SAtom.of(value.substring(1));
        }

        if (value.equalsIgnoreCase("nil")) {
            return SList.nil;
        }

        if (value.equalsIgnoreCase("true")) {
            return SAtom.of("true");
        }

        return SName.of(value);
    }

    private boolean isInteger(String value) {
        if (value.charAt(0) == '-') {
            value = value.substring(1);
        }
        if (value.isEmpty()) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            char chr = value.charAt(i);
            if (chr < '0' || chr > '9') {
                return false;
            }
        }
        return true;
    }

    private boolean isDecimal(String value) {
        if (value.charAt(0) == '-') {
            value = value.substring(1);
        }
        if (value.isEmpty()) {
            return false;
        }
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
