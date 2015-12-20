package slang.interpreter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Antoine Chauvin
 */
final class MockOutputStream extends OutputStream {
    private final StringBuilder queue = new StringBuilder();

    @Override
    public void write(int b) throws IOException {
        queue.appendCodePoint(b);
    }

    public String clear() {
        String result = queue.toString();
        queue.setLength(0);
        return result;
    }
}
