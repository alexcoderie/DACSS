import requestreply.*;
import messagemarshaller.*;
import registry.*;
import commons.Address;

import java.util.Scanner;

public class Client {
    public static void main(String[]args) {
        Address dispatcherAddress = new Entry("127.0.0.1", 9999);
        Requestor r = new Requestor("Client");
        Marshaller m = new Marshaller();
        Scanner scanner = new Scanner(System.in);

        Address serverAddress = lookupServer(dispatcherAddress, r, m);
        System.out.println(serverAddress.dest() + " " + serverAddress.port());

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

            Message msg = new Message("Client", userInput);
            byte[] bytes = m.marshal(msg);
            bytes = r.deliver_and_wait_feedback(serverAddress, bytes);
            Message answer = m.unmarshal(bytes);

            System.out.println("Client received message " + answer.data + " from " + answer.sender);
        }

        scanner.close();
    }

    //TO-DO IMPLEMENT LOGIC FOR LOOKING UP THE SERVER
    private static Address lookupServer(Address dispatcherAddress, Requestor r, Marshaller m) {

        String lookupRequest = "LOOKUP:Server";
        Message request = new Message("Client", lookupRequest);
        byte[] requestBytes = m.marshal(request);
        requestBytes = r.deliver_and_wait_feedback(dispatcherAddress, requestBytes);
        Message requestAnswer = m.unmarshal(requestBytes);

        System.out.println("Received message: " + requestAnswer.data + " from " + requestAnswer.sender);

        String[] parts = requestAnswer.data.split(":");
        String serverIP = parts[0];
        int port = Integer.parseInt(parts[1]);
        Address serverAddress = new Entry(serverIP, port);

        return serverAddress;
    }
}