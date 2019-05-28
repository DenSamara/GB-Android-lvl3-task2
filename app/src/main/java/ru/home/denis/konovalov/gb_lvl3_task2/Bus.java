package ru.home.denis.konovalov.gb_lvl3_task2;

import android.support.annotation.IntDef;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class Bus {
    @IntDef({TYPE_1, TYPE_2})
    public @interface SubscriptionType{}
    public static final byte TYPE_1 = 0;
    public static final byte TYPE_2 = 1;

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
