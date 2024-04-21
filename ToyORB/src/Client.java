import requestreply.*;
import messagemarshaller.*;
import registry.*;
import commons.Address;

import java.util.Scanner;

public class Client {
    public static void main(String[]args) {
        new Configuration();

        Address dest = Registry.instance().get("Server");
        Requestor r = new Requestor("Client");
        Marshaller m = new Marshaller();
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("Choose an operation: ");
            System.out.println("1. Get road info");
            System.out.println("2. Get temperature");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if(choice == 3) {
                break;
            }

            String operation = "";
            String parameter = "";

            switch(choice) {
                case 1:
                    operation = "get_road_info";
                    System.out.println("Enter road ID: ");
                    parameter = scanner.nextLine();
                    break;
                case 2:
                    operation = "get_temp";
                    System.out.println("Enter city: ");
                    parameter = scanner.nextLine();
                    break;
                default:
                    System.out.println("Invalid choice");
                    continue;
            }

            String userInput = operation.concat(":").concat(parameter);

            Message msg= new Message("Client", userInput);
            byte[] bytes = m.marshal(msg);
            bytes = r.deliver_and_wait_feedback(dest, bytes);
            Message answer = m.unmarshal(bytes);

            System.out.println("Client received message " + answer.data + " from " + answer.sender);
        }

        scanner.close();
    }
}