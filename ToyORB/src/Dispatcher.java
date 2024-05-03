import bytesendreceive.ByteReceiver;
import bytesendreceive.ByteSender;
import commons.Address;
import messagemarshaller.Marshaller;
import messagemarshaller.Message;
import registry.Entry;
import registry.Registry;
import requestreply.ByteStreamTransformer;
import requestreply.Replyer;

class DispatcherTransformer implements ByteStreamTransformer
{
    private MessageDispatcher originalServer;

    public DispatcherTransformer(MessageDispatcher s) {
        originalServer = s;
    }

    public byte[] transform(byte[] in) {
        Message msg;
        Marshaller m = new Marshaller();
        msg = m.unmarshal(in);

        Message answer = originalServer.get_answer(msg);

        byte[] bytes = m.marshal(answer);
        return bytes;
    }
}

class MessageDispatcher {
    public Message get_answer(Message msg) {
        System.out.println("Dispatcher received " + msg.data + " from " + msg.sender);

        String[] parts = msg.data.split(":");
        String command = parts[0];
        String name = parts[1];

        if(command.equals("REGISTER")) {
            int port = Integer.parseInt(parts[2]);
            register(name, port);
            return new Message("Dispatcher", "You are registered");
        } else if(command.equals("LOOKUP")) {
            Address destination = Registry.instance().get(name);
            String serverIP = destination.dest();
            String serverPort = String.valueOf(destination.port());
            String serverAddress = serverIP.concat(":").concat(serverPort);

            return new Message("Dispatcher", serverAddress);
        } else {
            return new Message("Dispatcher", "Invalid operation");
        }
    }

    private static void register(String name, int port) {
        Registry.instance().put(name, new Entry("127.0.0.1", port));
        System.out.println("Registered server " + name);
    }
}
public class Dispatcher {
    public static final int PORT = 9999;
    public static void main(String[] args) {
        Address dispatcherAddress = new Entry("127.0.0.1", PORT);
        Replyer replyer = new Replyer("Dispatcher", dispatcherAddress);
        ByteStreamTransformer transformer = new DispatcherTransformer(new MessageDispatcher());;

        System.out.println("Dispatcher started on port " + PORT);

        while(true) {
            replyer.receive_transform_and_send_feedback(transformer);
        }
    }
}
