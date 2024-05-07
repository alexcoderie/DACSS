package toyORB;

import commons.Address;
import messagemarshaller.Marshaller;
import messagemarshaller.Message;
import registry.Entry;
import requestreply.Requestor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ToyORB {
    private static Address dispatcherAddress = new Entry("127.0.0.1", 9999);

    public static void register(String name, Object object, int port) {

        Requestor requestor = new Requestor("ToyORB");
        Marshaller m = new Marshaller();
        Class<?>[] interfaces = object.getClass().getInterfaces();
        if (interfaces.length > 0) {
            String interfaceName = interfaces[0].getSimpleName();

            String registerRequest = "REGISTER:" + name + ":" + port + ":" + interfaceName;
            Message request = new Message("ToyORB", registerRequest);
            byte[] bytes = m.marshal(request);

            bytes = requestor.deliver_and_wait_feedback(dispatcherAddress, bytes);
            Message answer = m.unmarshal(bytes);

            System.out.println("Received message: " + answer.data + " from " + answer.sender);

            String proxyClassName = "serverproxies." + interfaceName + "ServerSideProxy";

            try{
                Class<?> proxyClass = Class.forName(proxyClassName);
                Constructor<?> constructor = proxyClass.getConstructor();
                Object proxyInstance = constructor.newInstance();
                Method startMethod = proxyClass.getDeclaredMethod("start");

                startMethod.invoke(proxyInstance);

            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T getObjectReference(String name) {
        Requestor requestor = new Requestor("ToyORB");
        Marshaller m = new Marshaller();
        String lookupRequest = "LOOKUP:" + name;
        Message request = new Message("ToyORB", lookupRequest);
        byte[] bytes = m.marshal(request);

        bytes = requestor.deliver_and_wait_feedback(dispatcherAddress, bytes);
        Message answer = m.unmarshal(bytes);

        String[] parts = answer.data.split(":");
        String serverIP = parts[0];
        int port = Integer.parseInt(parts[1]);
        String objectType = parts[2];

        Entry serverAddress = new Entry(serverIP, port);

        Class<?> proxyClass = null;
        try {
            proxyClass = Class.forName("clientproxies." + objectType + "ClientSideProxy");
            Constructor<?> constructor = proxyClass.getConstructor(Address.class);

            return (T) constructor.newInstance(serverAddress);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
