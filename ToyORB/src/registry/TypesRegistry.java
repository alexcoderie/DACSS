package registry;

import java.util.Hashtable;

public class TypesRegistry {
    private Hashtable hTable = new Hashtable();
    private static TypesRegistry _instance = null;

    private TypesRegistry() {
    }

    public static TypesRegistry instance() {
        if (_instance == null)
            _instance = new TypesRegistry();
        return _instance;
    }

    public void put(String theKey, String objectType) {
        hTable.put(theKey, objectType);
    }

    public String get(String aKey) {
        return (String) hTable.get(aKey);
    }
}
