package eventservice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventService {

    private final Class<?> eventClass=Event.class;
    protected List<Subscription> subscriptions;

    // Prevents direct instantiation of the event service - Singleton pattern
    private EventService() {
        subscriptions = new ArrayList<>();
    }

    static private EventService singleton = null;

    // Provides well-known access point to singleton EventService
    static public EventService instance() {
        if (singleton == null)
            singleton = new EventService();
        return singleton;
    }

    public void publish(Event event) {
        for (Subscription subscription : subscriptions) {
            if (subscription.eventType.isAssignableFrom(event.getClass())
                    && (subscription.filter == null || subscription.filter.apply(event)))
                invokeHandlerMethod(subscription.subscriber, event, subscription.eventType);
        }
    }

    public void invokeHandlerMethod(Subscriber subscriber, Event event, Class<?> eventType) {
        Class<?> subscriberClass = subscriber.getClass();

        Method[] methods = subscriberClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getParameterCount() == 1 && method.getParameterTypes()[0].isAssignableFrom(eventType)) {
                try {
                    // Invoke the handler method
                    method.invoke(subscriber, event);
                    return; // Only invoke the first matching handler method
                } catch (IllegalAccessException | InvocationTargetException e) {
                    // Handle reflection exceptions
                    e.printStackTrace(); // or log it
                }
            }
        }
    }

    public void subscribe(Subscriber subscriber, String handlerName, Filter filter)
            throws InvalidEventTypeException {
        Class<?> subscriberClass = subscriber.getClass();

        Method[] methods = subscriberClass.getMethods();
        Method handlerMethod = null;

        for (Method method : methods) {
            if (method.getName().equals(handlerName) && method.getParameterCount() == 1) {
                handlerMethod = method;
                break;
            }
        }

        if (handlerMethod == null) {
            throw new InvalidEventTypeException();
        }

        if (handlerMethod.getParameterCount() != 1) {
            throw new InvalidEventTypeException();
        }

        Class<?> eventType = handlerMethod.getParameterTypes()[0];

        if(!eventClass.isAssignableFrom(eventType)) {
            throw new InvalidEventTypeException();
        }
        Subscription subscription = new Subscription(eventType, filter, subscriber);
        if(!subscriptions.contains(subscription)) {
            subscriptions.add(subscription);
        }
    }

    public void unsubscribe(Class<?> eventType, Filter filter, Subscriber subscriber)
            throws InvalidEventTypeException {
        if (!eventClass.isAssignableFrom(eventType))
            throw new InvalidEventTypeException();
        subscriptions.remove(new Subscription(eventType, filter, subscriber));
    }

}


// Stores information about a single subscription
class Subscription {
    public Subscription(Class<?> anEventType, Filter aFilter, Subscriber aSubscriber) {
        eventType = anEventType;
        filter = aFilter;
        subscriber = aSubscriber;
    }

    public Class<?> eventType;
    public Filter filter;
    public Subscriber subscriber;
}
