public class Main {
    public static void main(String[] args) {
        ProductType carInsurance = new ProductType("Car Insurance");
        PropertyType brand = new PropertyType("brand", String.class);
        PropertyType age = new PropertyType("age", Integer.class);
        PropertyType enginePower = new PropertyType("enginePower", Integer.class);

        carInsurance.addPropertyType(brand);
        carInsurance.addPropertyType(age);
        carInsurance.addPropertyType(enginePower);

        carInsurance.setPriceRule(props ->
                1000 * ((String) props.get("brand").getValue()).length() - 10 * (Integer) props.get("age").getValue() + 5 * (Integer) props.get("enginePower").getValue()
        );

        Product mCarInsurance = new Product(carInsurance);
        mCarInsurance.setProperty("brand", "Toyota");
        mCarInsurance.setProperty("age", 10);
        mCarInsurance.setProperty("enginePower", 150);

        System.out.println("Price of my car insurance: " + mCarInsurance.calculatePrice());

        mCarInsurance.setProperty("enginePower", 200);
        System.out.println("Price of my car insurance: " + mCarInsurance.calculatePrice());

        carInsurance.removePropertyType(enginePower.getName());

        try {
            mCarInsurance.setProperty("enginePower", 200);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to set property enginePower: " + e.getMessage());
        }
    }
}
