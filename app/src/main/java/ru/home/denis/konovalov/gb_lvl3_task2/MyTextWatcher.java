package ru.home.denis.konovalov.gb_lvl3_task2;

import android.text.Editable;
import android.text.TextWatcher;

import io.reactivex.ObservableEmitter;

public class MyTextWatcher implements TextWatcher {

    private ObservableEmitter<String> emitter;

    public MyTextWatcher(ObservableEmitter<String> emitter){
        this.emitter = emitter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        emitter.onNext(s.toString());
    }
}
