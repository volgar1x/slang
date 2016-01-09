package slang.tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

import static slang.tokenizer.ConstToken.Type.*;

/**
 * @author Antoine Chauvin
 */
public final class Tokenizer implements Iterator<Token> {
    private final InputStream stream;
    private final Deque<Token> pending;
    private int line, col;
    private boolean string, set;

    public Tokenizer(InputStream stream) {
        this.stream = Objects.requireNonNull(stream, "stream");
        this.pending = new LinkedList<>();
        this.line = this.col = 1;
    }

    private boolean isWhitespace(int chr) {
        return Character.isWhitespace(chr);
    }

    private boolean isEndOfLine(int chr) {
        return chr == '\n';
    }

    private boolean isConstTokenType(int chr) {
        switch (chr) {
            case '(':
            case ')':
            case '[':
            case ']':
            case '{':
            case '}':
            case '#':
            case '"':
            case '\'':
            case ';':
                return true;

            default:
                return false;
        }
    }

    private boolean isValidIdentifierPart(int chr) {
        return !isWhitespace(chr) && !isConstTokenType(chr);
    }

    private int read() throws IOException {
        int chr = stream.read();
        if (isEndOfLine(chr)) {
            line += 1;
            col = 1;
        } else {
            col += 1;
        }
        return chr;
    }

    private int skipWhitespaces(int chr) throws IOException {
        while (isWhitespace(chr)) {
            chr = read();
        }
        return chr;
    }

    private int skipLine() throws IOException {
        while (true) {
            int chr = read();
            if (isEndOfLine(chr)) {
                break;
            }
        }
        return read();
    }

    @Override
    public boolean hasNext() {
        try {
            return !pending.isEmpty() || stream.available() > 0;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Token next() {
        try {
            return nextToken();
        } catch (IOException e) {
            return new ConstToken(0, 0, EOF);
        }
    }

    private Token nextToken() throws IOException {
        if (!pending.isEmpty()) {
            return pending.removeFirst();
        } else if (string) {
            string = false;
            return nextString(line, col);
        } else {
            return nextToken(read());
        }
    }

    private Token nextToken(int chr) throws IOException {
        chr = skipWhitespaces(chr);
        return nextToken(chr, line, col - 1);
    }

    private Token nextToken(int chr, int l, int c) throws IOException {
        switch (chr) {
            case -1:
                return new ConstToken(l, c, EOF);

            case ';'://comment
                return nextToken(skipLine());

            case '"'://string
                string = true;
                return new ConstToken(l, c, DOUBLE_QUOTE);

            case '('://list
                return new ConstToken(l, c, START_LIST);
            case ')'://list
                return new ConstToken(l, c, END_LIST);

            case '['://vector
                return new ConstToken(l, c, START_VECTOR);
            case ']'://vector
                return new ConstToken(l, c, END_VECTOR);

            case '{'://map
                return new ConstToken(l, c, START_MAP);
            case '}'://map
                if (set) {
                    set = false;
                    return new ConstToken(l, c, END_SET);
                }
                return new ConstToken(l, c, END_MAP);

            case '\''://quote
                return new ConstToken(l, c, QUOTE);

            case '#'://unquote | set
                Token tok = nextToken();
                if (tok.test(START_MAP)) {
                    set = true;
                    return new ConstToken(l, c, START_SET);
                } else {
                    pending.addFirst(tok);
                    return new ConstToken(l, c, UNQUOTE);
                }
        }

        return nextValue(chr, l, c);
    }

    private Token nextString(int l, int c) throws IOException {
        boolean escape = false;

        StringBuilder builder = new StringBuilder();

        int chr;
        while (true) {
            chr = read();

            if (escape) {
                escape = false;
            } else if (chr == '\\') {
                escape = true;
            } else if (chr == '"' || chr == -1) {
                break;
            }

            builder.appendCodePoint(chr);
        }

        pending.addFirst(new ConstToken(l, c + builder.length(),
                chr == -1 ? EOF : DOUBLE_QUOTE));

        return new ValueToken(l, c, builder.toString());
    }

    private Token nextValue(int chr, int l, int c) throws IOException {
        StringBuilder builder = new StringBuilder();

        while (isValidIdentifierPart(chr)) {
            builder.appendCodePoint(chr);

            chr = read();
        }

        if (isConstTokenType(chr)) {
            pending.addFirst(nextToken(chr, l, c + builder.length()));
        }

        return new ValueToken(l, c, builder.toString());
    }
}
