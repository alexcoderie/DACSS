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
                    && (subscription.filter == null || applyFilter(subscription, event)))
                try {
                    subscription.handlerMethod.invoke(subscription.subscriber, event);
                    return;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
        }
    }

    private boolean applyFilter(Subscription subscription, Event event) {
        try {
            return (boolean) subscription.filterMethod.invoke(subscription.filter, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void subscribe(Subscriber subscriber, String handlerName, Filter filter, String filterName)
            throws InvalidEventTypeException {
        Class<?> subscriberClass = subscriber.getClass();

        Method[] subscriberClassMethods = subscriberClass.getMethods();

        Method handlerMethod = null;
        Method filterMethod = null;

        for (Method sMethod : subscriberClassMethods) {
            if (sMethod.getName().equals(handlerName) && sMethod.getParameterCount() == 1) {
                handlerMethod = sMethod;
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

        if(filter != null) {
            Class<?> filterClass = filter.getClass();
            Method[] filterClassMethods = filterClass.getMethods();

            for (Method fMethod : filterClassMethods) {
                if (fMethod.getName().equals(filterName) && fMethod.getParameterCount() == 1) {
                    filterMethod = fMethod;
                    break;
                }
            }

            if (filterMethod == null) {
                throw new InvalidEventTypeException();
            }

            if (filterMethod.getParameterCount() != 1 || filterMethod.getParameterTypes()[0] != eventType || filterMethod.getReturnType() != boolean.class) {
                throw new InvalidEventTypeException();
            }
        }


        if(!eventClass.isAssignableFrom(eventType)) {
            throw new InvalidEventTypeException();
        }
        Subscription subscription = new Subscription(eventType, filter, subscriber, handlerMethod, filterMethod);
        if(!subscriptions.contains(subscription)) {
            subscriptions.add(subscription);
        }
    }


    public void unsubscribe(Class<?> eventType, Filter filter, Subscriber subscriber, Method handlerMethod, Method filterMethod)
            throws InvalidEventTypeException {
        if (!eventClass.isAssignableFrom(eventType))
            throw new InvalidEventTypeException();
        subscriptions.remove(new Subscription(eventType, filter, subscriber, handlerMethod, filterMethod));
    }

}


// Stores information about a single subscription
class Subscription {
    public Subscription(Class<?> anEventType, Filter aFilter, Subscriber aSubscriber, Method ahandlerMethod, Method afilterMethod) {
        eventType = anEventType;
        filter = aFilter;
        subscriber = aSubscriber;
        handlerMethod = ahandlerMethod;
        filterMethod = afilterMethod;
    }

    public Method handlerMethod;
    public Method filterMethod;
    public Class<?> eventType;
    public Filter filter;
    public Subscriber subscriber;
}
