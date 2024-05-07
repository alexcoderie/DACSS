package serverproxies;

import messagemarshaller.Message;

public interface Server {
    Message get_answer(Message msg);
}
