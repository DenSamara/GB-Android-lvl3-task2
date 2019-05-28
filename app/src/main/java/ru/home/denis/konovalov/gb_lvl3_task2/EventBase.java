package ru.home.denis.konovalov.gb_lvl3_task2;

import android.support.annotation.IntDef;

public abstract class EventBase {
    @IntDef({Subscribe, Unsubscribe, Hasfocus, Lostfocus})
    public @interface EventsType{}
    public static final byte Subscribe = 0;
    public static final byte Unsubscribe = 1;
    public static final byte Hasfocus = 2;
    public static final byte Lostfocus = 3;

    private @Bus.SubscriptionType int subscriptionType;
    private @EventsType int eventsType;

    public EventBase(@Bus.SubscriptionType int subscriptionType, @EventsType int eventsType){
        this.subscriptionType = subscriptionType;
        this.eventsType = eventsType;
    }

    public @EventsType int getSubscriptionType(){
        return subscriptionType;
    }
    public @EventsType int getEventsType(){
        return eventsType;
    }
}
