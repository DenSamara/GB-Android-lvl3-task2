package ru.home.denis.konovalov.gb_lvl3_task2;

import android.support.annotation.IntDef;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subjects.PublishSubject;

//TODO Шина должна слать сообщения о подписке/отписке
public class Bus {
    @IntDef({TYPE_1, TYPE_2})
    public @interface SubscriptionType{}
    public static final byte TYPE_1 = 0;
    public static final byte TYPE_2 = 1;

    private PublishSubject<EventBase> bus;
    private CompositeDisposable compositeDisposable;

    public Bus(){
        bus = PublishSubject.create();
        compositeDisposable = new CompositeDisposable();
    }

    public void send(EventBase object){
        bus.onNext(object);
    }

    public void subscribe(DisposableObserver<EventBase> d){
        Disposable disposable = bus.subscribeWith(d);
        compositeDisposable.add(disposable);
    }

    public void destroy(){
        compositeDisposable.dispose();
    }
}
