package slang.interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;

/**
 * @author Antoine Chauvin
 */
final class MockInputStream extends InputStream {
    private final StringBuilder tail = new StringBuilder();

    @Override
    public int read() throws IOException {
        if (tail.length() <= 0) {
            return -1;
        }
        int result = tail.codePointAt(0);
        tail.deleteCharAt(0);
        return result;
    }

    public StringBuilder append(Object obj) {
        return tail.append(obj);
    }

    public StringBuilder insert(int offset, Object obj) {
        return tail.insert(offset, obj);
    }

    public void trimToSize() {
        tail.trimToSize();
    }

    public void setLength(int newLength) {
        tail.setLength(newLength);
    }

    public char charAt(int index) {
        return tail.charAt(index);
    }

    public String substring(int start, int end) {
        return tail.substring(start, end);
    }

    public StringBuilder deleteCharAt(int index) {
        return tail.deleteCharAt(index);
    }

    public int length() {
        return tail.length();
    }

    public StringBuilder append(int i) {
        return tail.append(i);
    }

    public StringBuilder append(CharSequence s) {
        return tail.append(s);
    }

    public CharSequence subSequence(int start, int end) {
        return tail.subSequence(start, end);
    }

    public StringBuilder append(String str) {
        return tail.append(str);
    }

    public int lastIndexOf(String str) {
        return tail.lastIndexOf(str);
    }

    public StringBuilder insert(int offset, float f) {
        return tail.insert(offset, f);
    }

    public StringBuilder insert(int offset, char c) {
        return tail.insert(offset, c);
    }

    public StringBuilder insert(int offset, double d) {
        return tail.insert(offset, d);
    }

    public int offsetByCodePoints(int index, int codePointOffset) {
        return tail.offsetByCodePoints(index, codePointOffset);
    }

    public StringBuilder append(char[] str, int offset, int len) {
        return tail.append(str, offset, len);
    }

    public IntStream codePoints() {
        return tail.codePoints();
    }

    public StringBuilder append(long lng) {
        return tail.append(lng);
    }

    public StringBuilder insert(int offset, boolean b) {
        return tail.insert(offset, b);
    }

    public void ensureCapacity(int minimumCapacity) {
        tail.ensureCapacity(minimumCapacity);
    }

    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        tail.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    public StringBuilder append(StringBuffer sb) {
        return tail.append(sb);
    }

    public StringBuilder replace(int start, int end, String str) {
        return tail.replace(start, end, str);
    }

    public int codePointBefore(int index) {
        return tail.codePointBefore(index);
    }

    public StringBuilder insert(int dstOffset, CharSequence s, int start, int end) {
        return tail.insert(dstOffset, s, start, end);
    }

    public StringBuilder insert(int dstOffset, CharSequence s) {
        return tail.insert(dstOffset, s);
    }

    public IntStream chars() {
        return tail.chars();
    }

    public StringBuilder reverse() {
        return tail.reverse();
    }

    public StringBuilder append(double d) {
        return tail.append(d);
    }

    public int indexOf(String str) {
        return tail.indexOf(str);
    }

    public StringBuilder appendCodePoint(int codePoint) {
        return tail.appendCodePoint(codePoint);
    }

    public StringBuilder append(boolean b) {
        return tail.append(b);
    }

    public StringBuilder insert(int offset, int i) {
        return tail.insert(offset, i);
    }

    public int codePointCount(int beginIndex, int endIndex) {
        return tail.codePointCount(beginIndex, endIndex);
    }

    public StringBuilder append(CharSequence s, int start, int end) {
        return tail.append(s, start, end);
    }

    public void setCharAt(int index, char ch) {
        tail.setCharAt(index, ch);
    }

    public StringBuilder append(float f) {
        return tail.append(f);
    }

    public StringBuilder append(char c) {
        return tail.append(c);
    }

    public String substring(int start) {
        return tail.substring(start);
    }

    public int codePointAt(int index) {
        return tail.codePointAt(index);
    }

    public StringBuilder insert(int index, char[] str, int offset, int len) {
        return tail.insert(index, str, offset, len);
    }

    public int lastIndexOf(String str, int fromIndex) {
        return tail.lastIndexOf(str, fromIndex);
    }

    public StringBuilder delete(int start, int end) {
        return tail.delete(start, end);
    }

    public int capacity() {
        return tail.capacity();
    }

    public StringBuilder insert(int offset, String str) {
        return tail.insert(offset, str);
    }

    public StringBuilder insert(int offset, char[] str) {
        return tail.insert(offset, str);
    }

    public StringBuilder append(char[] str) {
        return tail.append(str);
    }

    public int indexOf(String str, int fromIndex) {
        return tail.indexOf(str, fromIndex);
    }

    public StringBuilder insert(int offset, long l) {
        return tail.insert(offset, l);
    }
}
