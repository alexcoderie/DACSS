package clientproxies;

import commons.Address;
import messagemarshaller.Marshaller;
import messagemarshaller.Message;
import requestreply.Requestor;

import cityinfoapp.CityInfo;
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
	}	@Override
	public String getRoadInfo(int param0) {
		String message = "getRoadInfo:" + param0;
		String answer = sendRequest(message);
		return answer;
	}

	@Override
	public String getTemperature(String param0) {
		String message = "getTemperature:" + param0;
		String answer = sendRequest(message);
		return answer;
	}

}
