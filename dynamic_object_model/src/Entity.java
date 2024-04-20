import java.util.HashMap;
import java.util.Map;

public class Entity {
    private EntityType entityType;
    private Map<String, Property> properties;

    public Entity(EntityType entityType) {
        this.entityType = entityType;
        properties = new HashMap<>();
    }

    public void setProperty(String name, Object value) {
        PropertyType propertyType = entityType.getPropertyType(name);

        if(propertyType != null) {
            if(propertyType.getType().isInstance(value)) {
                properties.put(name, new Property(name, value));
            } else {
                throw new IllegalArgumentException("Invalid value " + value + " from property " + name);
            }
        } else {
            throw new IllegalArgumentException("Property " + name + " is not allowed for product " + entityType.getName());
        }
    }

    public double calculatePrice() {
        if(entityType.getPriceRule() != null) {
            return entityType.getPriceRule().apply(properties);
        } else {
            throw new IllegalStateException("No price rule defined for this product type");
        }
    }
}
