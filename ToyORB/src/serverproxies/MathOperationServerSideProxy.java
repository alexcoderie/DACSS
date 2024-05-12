package serverproxies;

import commons.Address;
import mathoperationapp.MathOperation;
import mathoperationapp.MathOperationImpl;
import messagemarshaller.Message;
import registry.Entry;
import requestreply.ByteStreamTransformer;
import requestreply.Replyer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class MathOperationServer implements Server {
	public MathOperation serverObject;

	public MathOperationServer() {
		this.serverObject = new MathOperationImpl();
	}

	public Message get_answer(Message msg) {
		System.out.println("Server received " + msg.data + " from " + msg.sender);

		String[] parts = msg.data.split(":");
		String operation = parts[0];

		try {
			String methodName = operation;
			Object[] parameters = new Object[parts.length - 1];

			for(int i = 1; i < parts.length; i++) {
				parameters[i - 1] = parts[i];
			}
			Method[] methods = serverObject.getClass().getMethods();
			Method method = null;
			for(Method m : methods) {
				if(m.getName().equals(methodName) && m.getParameterCount() == parameters.length) {
					boolean match = true;
					Parameter[] methodParams = m.getParameters();
					for(int i = 0; i < parameters.length; i++) {
						Class<?> paramType = methodParams[i].getType();
						parameters[i] = convertToType(parameters[i].toString(), paramType);
						if(parameters[i] == null) {
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
			Object result = method.invoke(serverObject, parameters);
			return new Message("MathOperationServer" , result.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new Message("MathOperationServer", "Error: " + e.getMessage());
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

public class MathOperationServerSideProxy {
	Address myAddr = new Entry("127.0.0.1", 1112);

	public void start() {
		ByteStreamTransformer transformer = new ServerTransformer(new MathOperationServer());
		Replyer r = new Replyer("MathOperationServer", myAddr);

		while(true) {
			r.receive_transform_and_send_feedback(transformer);
		}
	}
}
