import java.util.stream.Stream;

public class UserAccount {
    private String username;
    private String email;
    private Address invoiceAddress;
    private Address deliveryAddress;

    public UserAccount(String username, String email, Address invoiceAddress, Address deliveryAddress) {
        this.username = username;
        this.email = email;
        this.invoiceAddress = invoiceAddress;
        this.deliveryAddress = deliveryAddress;
    }
}
