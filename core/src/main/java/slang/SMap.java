package slang;

import java.util.Map;
import java.util.Set;

/**
 * @author Antoine Chauvin
 */
public interface SMap {
    Object get(Object key);
    boolean containsKey(Object key);
    boolean containsValue(Object value);
    Set<Map.Entry<Object, Object>> entrySet();

    SMap with(Object key, Object value);
    SMap without(Object key);

    interface Builder {
        void add(Object key, Object value);
        SMap build();
    }
}
