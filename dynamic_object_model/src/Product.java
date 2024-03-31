import java.util.HashMap;
import java.util.Map;

public class Product {
    private ProductType productType;
    private Map<String, Property> properties;

    public Product(ProductType productType) {
        this.productType = productType;
        properties = new HashMap<>();
    }

    public void setProperty(String name, Object value) {
        PropertyType propertyType = productType.getPropertyType(name);

        if(propertyType != null) {
            if(propertyType.getType().isInstance(value)) {
                properties.put(name, new Property(name, value));
            } else {
                throw new IllegalArgumentException("Invalid value " + value + " from property " + name);
            }
        } else {
            throw new IllegalArgumentException("Property " + name + " is not allowed for product " + productType.getName());
        }
    }

    public double calculatePrice() {
        if(productType.getPriceRule() != null) {
            return productType.getPriceRule().apply(properties);
        } else {
            throw new IllegalStateException("No price rule defined for this product type");
        }
    }
}
