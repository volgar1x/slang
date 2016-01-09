package slang.tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Antoine Chauvin
 */
public final class Tokenizer implements Iterator<Token> {
    private final InputStream stream;

    private boolean string, set;
    private final Deque<Token> queued;
    private int curLine, curColumn;

    public Tokenizer(InputStream stream) {
        this.stream = stream;
        this.string = false;
        this.set = false;
        this.queued = new LinkedList<>();
        this.curLine = this.curColumn = 0;
    }

    private void incLine() {
        this.curLine++;
        this.curColumn = 0;
    }

    private void incColumn() {
        this.curColumn++;
    }

    private int incPos(int chr) {
        if (chr == '\n') {
            incLine();
        } else {
            incColumn();
        }
        return chr;
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
    public Token next() {
        try {
            return safeNext();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ConstToken newConst(ConstToken.Type type) {
        return new ConstToken(curLine, curColumn, type);
    }

    private ValueToken newValue(String value) {
        return new ValueToken(curLine, curColumn, value);
    }

    private int read() throws IOException {
        return incPos(stream.read());
    }

    private int skipWhitespaces(int chr) throws IOException {
        while (Character.isWhitespace(chr)) {
            chr = read();
        }
        return chr;
    }

    private int skipLine() throws IOException {
        while (true) {
            int chr = read();
            if (chr == '\n' || chr == -1) {
                break;
            }
        }
        return read();
    }

    private Token safeNext() throws IOException {
        if (!queued.isEmpty()) {
            return queued.removeFirst();
        }

        if (string) {
            int chr = read();
            StringBuilder buf = new StringBuilder();
            while (chr != -1 && chr != '"') {
                buf.append((char) chr);
                chr = read();
            }
            string = false;
            if (chr == -1) {
                queued.addFirst(newConst(ConstToken.Type.EOF));
            } else {
                queued.addFirst(newConst(ConstToken.Type.DOUBLE_QUOTE));
            }
            return newValue(buf.toString());
        }

        int chr = skipWhitespaces(read());

        while (chr == ';') {
            chr = skipWhitespaces(skipLine());
        }

        switch (chr) {
            case -1:   return newConst(ConstToken.Type.EOF);
            case '(':  return newConst(ConstToken.Type.START_LIST);
            case ')':  return newConst(ConstToken.Type.END_LIST);
            case '{':  return newConst(ConstToken.Type.START_MAP);
            case '}':
                if (set) {
                    set = false;
                    return newConst(ConstToken.Type.END_SET);
                }
                return newConst(ConstToken.Type.END_MAP);
            case '[':  return newConst(ConstToken.Type.START_VECTOR);
            case ']':  return newConst(ConstToken.Type.END_VECTOR);
            case '\'': return newConst(ConstToken.Type.QUOTE);
            case '#':
                Token nextToken = safeNext();
                if (nextToken.test(ConstToken.Type.START_MAP)) {
                    set = true;
                    return newConst(ConstToken.Type.START_SET);
                }
                queued.addFirst(nextToken);
                return newConst(ConstToken.Type.UNQUOTE);

            case '"':
                string = true;
                return newConst(ConstToken.Type.DOUBLE_QUOTE);
        }

        StringBuilder buf = new StringBuilder();
        while (!Character.isWhitespace(chr) && !isEndingToken(chr)) {
            buf.append((char) chr);
            chr = read();
        }
        if (isEndingToken(chr)) {
            queued.addFirst(asEndingToken(chr));
        }

        return newValue(buf.toString());
    }

    private Token asEndingToken(int chr) {
        switch (chr) {
            case -1:  return newConst(ConstToken.Type.EOF);
            case ')': return newConst(ConstToken.Type.END_LIST);
            case '}':
                if (set) {
                    set = false;
                    return newConst(ConstToken.Type.END_SET);
                }
                return newConst(ConstToken.Type.END_MAP);
            case ']': return newConst(ConstToken.Type.END_VECTOR);
            default:  return null;
        }
    }

    private boolean isEndingToken(int chr) {
        switch (chr) {
            case ')':
            case '}':
            case ']':
                return true;
        }
        return false;
    }
}
