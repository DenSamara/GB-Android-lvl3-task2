package ru.home.denis.konovalov.gb_lvl3_task2;

public class EventType1 extends EventBase{

    public EventType1(@EventsType int eventsType){
        super(Bus.TYPE_1, eventsType);
    }
}
