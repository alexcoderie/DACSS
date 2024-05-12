package clientproxies;

import commons.Address;
import messagemarshaller.Marshaller;
import messagemarshaller.Message;
import requestreply.Requestor;

import mathoperationapp.MathOperation;
public class MathOperationClientSideProxy implements MathOperation {
	private Address serverAddress;

	public MathOperationClientSideProxy(Address serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String sendRequest(String request) {
		Requestor r = new Requestor("MathOperationClient");
		Marshaller m = new Marshaller();

		Message msg = new Message("MathOperationClient", request);
		byte[] bytes = m.marshal(msg);
		bytes = r.deliver_and_wait_feedback(serverAddress, bytes);
		Message answer = m.unmarshal(bytes);

		return answer.data;
	}	@Override
	public String doAdd(float param0, float param1) {
		String message = "doAdd:" + param0 + ":" + param1;
		String answer = sendRequest(message);
		return answer;
	}

	@Override
	public String doSqr(float param0) {
		String message = "doSqr:" + param0;
		String answer = sendRequest(message);
		return answer;
	}

}
