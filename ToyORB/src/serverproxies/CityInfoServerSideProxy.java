package serverproxies;

import commons.Address;
import cityinfoapp.CityInfo;
import cityinfoapp.CityInfoImpl;
import messagemarshaller.Message;
import registry.Entry;
import requestreply.ByteStreamTransformer;
import requestreply.Replyer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class CityInfoServer implements Server{
    public CityInfo cityInfo;

    public CityInfoServer() {
        this.cityInfo = new CityInfoImpl();
    }

    public Message get_answer(Message msg) {
        System.out.println("Server received " + msg.data + " from " + msg.sender);

        String[] parts = msg.data.split(":");
        String operation = parts[0];

        try {
            String methodName = operation;
            Object[] parameters = new Object[parts.length - 1];

            for(int i = 1; i < parts.length; i++) {
                parameters[i-1] = parts[i];
            }

            Method[] methods = cityInfo.getClass().getMethods();
            Method method = null;
            for(Method m : methods) {
                if(m.getName().equals(methodName) && m.getParameterCount() == parameters.length) {
                    boolean match = true;
                    Parameter[] methodParams = m.getParameters();
                    for(int i = 0; i < parameters.length; i++) {
                        Class<?> paramType = methodParams[i].getType();
                        parameters[i] = convertToType(parameters[i].toString(), paramType);
                        if(parameters[i] == null) {
                            System.out.println("In second if");
                            match = false;
                            break;
                        }
                    }

                    if(match) {
                        method = m;
                        break;
                    }
                }
            }

            if(method == null) {
                throw new NoSuchMethodException("Method not found: " + methodName);
            }

            Object result = method.invoke(cityInfo, parameters);
            return new Message("CityInfoServer", result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Message("CityInfoServer", "Error: " + e.getMessage());
        }
    }

    private Object convertToType(String param, Class<?> targetType) {
        if(targetType == String.class) {
            return param;
        } else if(targetType == Integer.class || targetType == int.class) {
            return Integer.parseInt(param);
        } else if(targetType == Float.class || targetType == float.class) {
            return Float.parseFloat(param);
        }

        return null;
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
