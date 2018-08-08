package github.hotstu.maotai.engine;

import java.util.HashSet;

/**
 * @author hglf
 * @since 2018/8/2
 */
public class EventFilter{

    private final HashSet<String> regesteredEvents;

    public EventFilter() {
        regesteredEvents = new HashSet<>();
    }

    public void regester(String eventName) {
        regesteredEvents.add(eventName);
    }

    public void unRegester(String eventName) {
        regesteredEvents.remove(eventName);
    }

    public boolean isRegestered(String eventName) {
        return regesteredEvents.contains(eventName);
    }

    public void clear() {
        regesteredEvents.clear();
    }


}
