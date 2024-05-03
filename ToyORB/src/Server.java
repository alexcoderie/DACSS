import bytesendreceive.ByteSender;
import requestreply.*;
import messagemarshaller.*;
import registry.*;
import commons.Address;

class ServerTransformer implements ByteStreamTransformer
{
    private MessageServer originalServer;

    public ServerTransformer(MessageServer s) {
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

class MessageServer {
    public Message get_answer(Message msg) {
        System.out.println("Server received " + msg.data + " from " + msg.sender);

        String[] parts = msg.data.split(":");
        String operation = parts[0];
        String parameter = parts[1];

        if(operation.equals("get_road_info")) {
            int roadID = Integer.parseInt(parameter);
            String roadInfo = get_road_info(roadID);
            return new Message("Server", roadInfo);
        } else if(operation.equals("get_temp")) {
            String city = parameter;
            String temp = get_temp(city);
            return new Message("Server", temp);
        } else {
            return new Message("Server", "Invalid operation");
        }
    }

    private String get_temp(String city) {
        return "Temperature for " + city + ": 25 C";
    }

    private String get_road_info(int roadID) {
        return "Road info for road ID " + roadID + ": Some information";
    }
}

public class Server {
    public static void main(String args[]) {
        Address dispatcherAddress = new Entry("127.0.0.1", 9999);
        Address myAddr = new Entry("127.0.0.1", 1111);
        registerServer(dispatcherAddress);

        ByteStreamTransformer transformer = new ServerTransformer(new MessageServer());
        Replyer r = new Replyer("Server", myAddr);

        while (true) {
            r.receive_transform_and_send_feedback(transformer);
        }
    }

    private static void registerServer(Address dispatcherAddress) {
        Requestor requestor = new Requestor("Server");
        Marshaller m = new Marshaller();
        String registerRequest = "REGISTER:Server:1111";
        Message request = new Message("Server", registerRequest);
        byte[] bytes = m.marshal(request);

        bytes = requestor.deliver_and_wait_feedback(dispatcherAddress, bytes);
        Message answer = m.unmarshal(bytes);

        System.out.println("Received message: " + answer.data + " from " + answer.sender);
    }
}