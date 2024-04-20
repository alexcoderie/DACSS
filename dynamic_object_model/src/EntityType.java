import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EntityType {
    private String name;
    private Map<String, PropertyType> allowedProperties;
    private Function<Map<String, Property>, Integer> priceRule;

    public EntityType(String name) {
        this.name = name;
        this.allowedProperties = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void addPropertyType(PropertyType propertyType) {
        allowedProperties.put(propertyType.getName(), propertyType);
    }

    public void removePropertyType(String name) {
        allowedProperties.remove(name);
    }

    public PropertyType getPropertyType(String name) {
        return allowedProperties.get(name);
    }

    public Map<String, PropertyType> getAllowedProperties() {
        return allowedProperties;
    }

    public void setPriceRule(Function<Map<String, Property>, Integer> priceRule) {
        this.priceRule = priceRule;
    }

    public Function<Map<String, Property>, Integer> getPriceRule() {
        return priceRule;
    }
}
