package slang;

/**
 * @author Antoine Chauvin
 */
public interface SMap {
    Object get(Object key);
    boolean containsKey(Object key);
    boolean containsValue(Object value);

    SMap with(Object key, Object value);
    SMap without(Object key);

    interface Builder {
        void add(Object key, Object value);
        SMap build();
    }
}
