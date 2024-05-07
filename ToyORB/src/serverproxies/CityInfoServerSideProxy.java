package serverproxies;

import commons.Address;
import infoapp.CityInfo;
import infoapp.CityInfoImpl;
import messagemarshaller.Message;
import registry.Entry;
import requestreply.ByteStreamTransformer;
import requestreply.Replyer;

class CityInfoServer implements Server{
    public Message get_answer(Message msg) {
        System.out.println("Server received " + msg.data + " from " + msg.sender);
        CityInfo cityInfo = new CityInfoImpl();

        String[] parts = msg.data.split(":");
        String operation = parts[0];
        String parameter = parts[1];

        if(operation.equals("get_road_info")) {
            int roadID = Integer.parseInt(parameter);
            String roadInfo = cityInfo.getRoadInfo(roadID);
            return new Message("CityInfoServer", roadInfo);
        } else if(operation.equals("get_temp")) {
            String city = parameter;
            String temp = String.valueOf(cityInfo.getTemperature(city));
            return new Message("CityInfoServer", temp);
        } else {
            return new Message("CityInfoServer", "Invalid operation");
        }
    }
}

public class CityInfoServerSideProxy {
    Address myAddr = new Entry("127.0.0.1", 1111);

    public void start() {
        ByteStreamTransformer transformer = new ServerTransformer(new CityInfoServer());
        Replyer r = new Replyer("CityInfoServer", myAddr);

        while (true) {
            r.receive_transform_and_send_feedback(transformer);
        }
    }
}
