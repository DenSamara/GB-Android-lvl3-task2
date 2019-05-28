package ru.home.denis.konovalov.gb_lvl3_task2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView textView;
    private EditText editText;
    private AppCompatButton btSubscribe;
    private AppCompatButton btUnsubscribe;

    private DisposableObserver<String> observer;
    private ObservableOnSubscribe<String> observableOnSubscribe;

    private MyTextWatcher textWatcher;
    private boolean isSubscribed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.label);
        editText = findViewById(R.id.text);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EventType2 event = new EventType2(hasFocus ? EventType2.Hasfocus : EventType2.Lostfocus);
                BusManager.getInstance().getBus()
                        .send(event);
            }
        });

        btSubscribe = findViewById(R.id.subscribe);
        btUnsubscribe = findViewById(R.id.unsubscribe);

        btSubscribe.setOnClickListener(this);
        btUnsubscribe.setOnClickListener(this);

        observableOnSubscribe = new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                textWatcher = new MyTextWatcher(emitter);
                editText.addTextChangedListener(textWatcher);
            }
        };

        //Подписываться на EventBus нужно только один раз, при создании активити
        if (savedInstanceState == null) {
            subscribeToBus();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO всегда подписываемся. Не корректно обрабатывается поворот
        BusManager.getInstance().getBus().send(new EventType1(EventType1.Subscribe));
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusManager.getInstance().getBus().send(new EventType1(EventType1.Unsubscribe));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subscribe:
                BusManager.getInstance().getBus()
                        .send(new EventType1(EventType1.Subscribe));
                break;
            case R.id.unsubscribe:
                BusManager.getInstance().getBus()
                        .send(new EventType1(EventType1.Unsubscribe));
                break;
        }
    }

    private void subscribe() {
        isSubscribed = true;

        Observable observable = Observable.create(observableOnSubscribe)
                .observeOn(AndroidSchedulers.mainThread());

        observer = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                textView.setText(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        observable.subscribe(observer);
    }

    private void unsubscribe() {
        isSubscribed = false;
        observer.dispose();

        editText.removeTextChangedListener(textWatcher);
    }

    private void enableButton(boolean value) {
        btSubscribe.setEnabled(!value);
        btUnsubscribe.setEnabled(value);
    }

    @SuppressLint("CheckResult")
    private void subscribeToBus(){
        BusManager.getInstance().getBus().toObservable().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                EventBase event = (EventBase) o;
                switch (event.getEventsType()) {
                    case EventBase.Subscribe:
                        subscribe();
                        break;
                    case EventBase.Unsubscribe:
                        unsubscribe();
                        break;
                    default:
                        GlobalProc.logE(TAG, "Subscription 1. EventsType: " + event.getEventsType());
                        break;
                }
                enableButton(isSubscribed);
            }
        });

        BusManager.getInstance().getBus().toObservable().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                EventBase event = (EventBase) o;
                switch (event.getEventsType()) {
                    case EventBase.Hasfocus:
                        GlobalProc.logE(TAG, "Has focus");
                        break;
                    case EventBase.Lostfocus:
                        GlobalProc.logE(TAG, "Lost focus");
                        break;
                    default:
                        GlobalProc.logE(TAG, "Subscription 2. EventsType: " + event.getEventsType());
                        break;
                }
            }
        });
    }
}
