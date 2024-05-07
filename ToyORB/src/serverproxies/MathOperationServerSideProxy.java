package serverproxies;

import commons.Address;
import mathapp.MathOperation;
import mathapp.MathOperationImpl;
import messagemarshaller.Message;
import registry.Entry;
import requestreply.ByteStreamTransformer;
import requestreply.Replyer;

class MathOperationServer implements Server{
    @Override
    public Message get_answer(Message msg) {
        System.out.println("Server received " + msg.data + " from " + msg.sender);
        MathOperation mathOperation = new MathOperationImpl();

        String[] parts = msg.data.split(":");
        String operation = parts[0];

        if(operation.equals("do_add")) {
            float a = Float.valueOf(parts[1]);
            float b = Float.valueOf(parts[2]);
            String result = String.valueOf(mathOperation.doAdd(a, b));

            return new Message("MathOperationServer", result);
        } else if(operation.equals("do_sqr")) {
            float a = Float.valueOf(parts[1]);
            String result = String.valueOf(mathOperation.doSqr(a));
            return new Message("MathOperationServer", result);
        } else {
            return new Message("MathOperationServer", "Invalid operation");
        }
    }
}

public class MathOperationServerSideProxy {
    Address myAddr = new Entry("127.0.0.1", 1112);

    public void start() {
        ByteStreamTransformer transformer = new ServerTransformer(new MathOperationServer());
        Replyer r = new Replyer("MathOperationServer", myAddr);

        while (true) {
            r.receive_transform_and_send_feedback(transformer);
        }
    }
}
