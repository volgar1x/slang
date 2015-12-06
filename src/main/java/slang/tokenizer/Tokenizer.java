package slang.tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Antoine Chauvin
 */
public final class Tokenizer implements Iterator<TokenInterface> {
    private final InputStream stream;

    private boolean string;
    private final Deque<TokenInterface> queued;

    public Tokenizer(InputStream stream) {
        this.stream = stream;
        this.string = false;
        this.queued = new LinkedList<>();
    }

    @Override
    public boolean hasNext() {
        try {
            return stream.available() > 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TokenInterface next() {
        try {
            return safeNext();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int skipWhitespaces(int chr) throws IOException {
        while (Character.isWhitespace(chr)) {
            chr = stream.read();
        }
        return chr;
    }

    private TokenInterface safeNext() throws IOException {
        if (!queued.isEmpty()) {
            return queued.removeFirst();
        }

        if (string) {
            int chr = stream.read();
            StringBuilder buf = new StringBuilder();
            while (chr != -1 && chr != '"') {
                buf.append((char) chr);
                chr = stream.read();
            }
            string = false;
            if (chr == -1) {
                queued.addLast(ConstToken.EOF);
            } else {
                queued.addLast(ConstToken.DOUBLE_QUOTE);
            }
            return Token.of(buf.toString());
        }

        int chr = skipWhitespaces(stream.read());

        switch (chr) {
            case -1:   return ConstToken.EOF;
            case '(':  return ConstToken.START_LIST;
            case ')':  return ConstToken.END_LIST;
            case '\'': return ConstToken.QUOTE;
            case '#':  return ConstToken.UNQUOTE;

            case '"':
                string = true;
                return ConstToken.DOUBLE_QUOTE;
        }

        StringBuilder buf = new StringBuilder();
        while (!Character.isWhitespace(chr) && !isEndingToken(chr)) {
            buf.append((char) chr);
            chr = stream.read();
        }
        if (isEndingToken(chr)) {
            queued.addLast(asEndingToken(chr));
        }

        return Token.of(buf.toString());
    }

    private TokenInterface asEndingToken(int chr) {
        switch (chr) {
            case -1:  return ConstToken.EOF;
            case ')': return ConstToken.END_LIST;
            default:  return null;
        }
    }

    private boolean isEndingToken(int chr) {
        return asEndingToken(chr) != null;
    }
}
