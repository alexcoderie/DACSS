package serverproxies;

import messagemarshaller.Marshaller;
import messagemarshaller.Message;
import requestreply.ByteStreamTransformer;

public class ServerTransformer implements ByteStreamTransformer {
    private Server originalServer;

    public ServerTransformer(Server s) {
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
