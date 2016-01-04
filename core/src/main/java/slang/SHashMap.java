package slang;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Antoine Chauvin
 */
public final class SHashMap extends HashMap<Object, Object> implements SMap {
    private void _putAll(SHashMap map) {
        super.putAll(map);
    }

    private void _put(Object key, Object value) {
        super.put(key, value);
    }

    private void _remove(Object key) {
        super.remove(key);
    }

    public static class Builder implements SMap.Builder {

        SHashMap map = new SHashMap();

        @Override
        public void add(Object key, Object value) {
            map._put(key, value);
        }

        @Override
        public SHashMap build() {
            return map;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Object get(Object key) {
        Object value = super.get(key);
        if (value == null) {
            return SList.nil;
        }
        return value;
    }

    @Override
    public SMap with(Object key, Object value) {
        SHashMap result = new SHashMap();
        result._putAll(this);
        result._put(key, value);
        return result;
    }

    @Override
    public SMap without(Object key) {
        SHashMap result = new SHashMap();
        result._putAll(this);
        result._remove(key);
        return result;
    }

    @Override
    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<?, ?> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace(Object key, Object oldValue, Object newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object replace(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object computeIfAbsent(Object key, Function<? super Object, ?> mappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object computeIfPresent(Object key, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object compute(Object key, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object merge(Object key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(BiFunction<? super Object, ? super Object, ?> function) {
        throw new UnsupportedOperationException();
    }
}
