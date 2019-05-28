package ru.home.denis.konovalov.gb_lvl3_task2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.reactivestreams.Subscription;

import java.util.List;
import java.util.Observer;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView textView;
    private EditText editText;
    private AppCompatButton btSubscribe;
    private AppCompatButton btUnsubscribe;

    private Observable observable;
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscribe();
        enableButton(isSubscribed);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unsubscribe();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.subscribe:
                subscribe();
                break;
            case R.id.unsubscribe:
                unsubscribe();
                break;
        }
        enableButton(isSubscribed);
    }

    private void subscribe(){
        isSubscribed = true;

        observable = Observable.create(observableOnSubscribe)
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

    private void unsubscribe(){
        isSubscribed = false;
        observer.dispose();

        editText.removeTextChangedListener(textWatcher);
    }

    private void enableButton(boolean value){
        btSubscribe.setEnabled(!value);
        btUnsubscribe.setEnabled(value);
    }
}
