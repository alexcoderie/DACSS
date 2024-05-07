package clientproxies;

import commons.Address;
import infoapp.CityInfo;
import messagemarshaller.Marshaller;
import messagemarshaller.Message;
import requestreply.Requestor;

import java.net.spi.InetAddressResolver;

public class CityInfoClientSideProxy implements CityInfo {
    private Address serverAddress;

    public CityInfoClientSideProxy(Address serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String sendRequest(String request) {
        Requestor r = new Requestor("CityInfoClient");
        Marshaller m = new Marshaller();

        Message msg = new Message("CityInfoClient", request);
        byte[] bytes = m.marshal(msg);
        bytes = r.deliver_and_wait_feedback(serverAddress, bytes);
        Message answer = m.unmarshal(bytes);

        return answer.data;
    }
    @Override
    public String getRoadInfo(int roadID) {

        String message = "get_road_info:" + roadID;
        String answer = sendRequest(message);

        return answer;
    }

    @Override
    public float getTemperature(String city) {
        String message = "get_temp:" + city;
        String answer = sendRequest(message);

        return Float.parseFloat(answer);
    }
}
