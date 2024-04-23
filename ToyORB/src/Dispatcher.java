import bytesendreceive.ByteReceiver;
import bytesendreceive.ByteSender;
import commons.Address;
import messagemarshaller.Marshaller;
import messagemarshaller.Message;
import registry.Entry;
import registry.Registry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Dispatcher {
    public static final int PORT = 9999;
    public static void main(String[] args) {
//            ServerSocket dispatcherSocket = new ServerSocket(PORT);
        Registry.instance().put("Dispatcher", new Entry("127.0.0.1", PORT));
        Address dispatcherAddress = Registry.instance().get("Dispatcher");
        ByteReceiver br = new ByteReceiver("Dispatcher", dispatcherAddress);
        ByteSender bs = new ByteSender("Dispatcher");
        Marshaller m = new Marshaller();

        System.out.println("Dispatcher started on port " + PORT);

        while(true) {
//                Socket socket = dispatcherSocket.accept();
            new Thread(new ClientHandler(dispatcherAddress, br, bs, m)).start();
        }
    }

    private static class ClientHandler implements Runnable {
//        private final Socket socket;
//
//
//        ClientHandler(Socket socket) {
//            this.socket = socket;
//        }

        private Address dispatcherAddress;
        private ByteReceiver br;
        private ByteSender bs;
        private Marshaller m;
        ClientHandler(Address dispatcherAddress, ByteReceiver br, ByteSender bs, Marshaller m) {
            this.dispatcherAddress = dispatcherAddress;
            this.br = br;
            this.bs = bs;
            this.m = m;
        }

        @Override
        public void run() {
            byte[] data = br.receive();
            Message request = m.unmarshal(data);
            System.out.println("Received request: " + request.data);

            String[] parts = request.data.split(":");
            String command = parts[0];

            if(command.equals("REGISTER")) {
                String name = parts[1];
                int port = Integer.parseInt(parts[2]);
                register(name, port);
                System.out.println("Registered " + Registry.instance().get(name).dest() + " " + Registry.instance().get(name).port() );
            } else if(command.equals("LOOKUP")) {
                String name = parts[1];
                Address destination = Registry.instance().get(name);
                Message message = new Message("Dispatcher", destination.toString());

                byte[] bytes = m.marshal(message);
                Address sender = Registry.instance().get(request.sender);
                bs.deliver(sender, bytes);
            } else {
                System.err.println("Invalid command");
            }
        }

        private static void register(String name, int port) {
            Registry.instance().put(name, new Entry("127.0.0.1", port));
            System.out.println("Registered server " + name);
        }
    }
}
