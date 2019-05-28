package ru.home.denis.konovalov.gb_lvl3_task2;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class Bus {
    private PublishSubject<Object> bus;

    public Bus(){
         bus = PublishSubject.create();
    }

    public void send(Object object){
        bus.onNext(object);
    }

    public Observable<Object> toObservable(){
        return bus;
    }
}
