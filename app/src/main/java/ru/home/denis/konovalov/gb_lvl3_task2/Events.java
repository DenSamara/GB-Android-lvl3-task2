package ru.home.denis.konovalov.gb_lvl3_task2;

import android.support.annotation.IntDef;
import android.view.View;

public class Events {
    @IntDef({Subscribe, Unsubscribe})
    public @interface EventsType{}
    public static final byte Subscribe = 0;
    public static final byte Unsubscribe = 1;

    private @EventsType int eventsType;
    private View view;

    public Events(@EventsType int eventsType, View v){
        this.eventsType = eventsType;
        view = v;
    }

    public @EventsType int getEventsType(){
        return eventsType;
    }

    public View getView(){
        return view;
    }
}
