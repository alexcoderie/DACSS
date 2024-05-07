package clientproxies;

import commons.Address;
import mathapp.MathOperation;
import messagemarshaller.Marshaller;
import messagemarshaller.Message;
import requestreply.Requestor;

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
    }
    @Override
    public float doAdd(float a, float b) {
        String message = "do_add:" + a + ":" + b;
        String answer = sendRequest(message);

        return Float.valueOf(answer);
    }

    @Override
    public double doSqr(float a) {
        String message = "do_sqr:" + a;
        String answer = sendRequest(message);

        return Float.valueOf(answer);
    }
}
