package com.fairhr.module_support.utils;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;


public class RxBus {
    private static volatile RxBus defaultInstance;
    private final Subject bus;

    public RxBus() {
        this.bus = PublishSubject.create().toSerialized();
    }

    public static RxBus getDefault() {
        RxBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    public void post(Object o) {
        this.bus.onNext(o);
    }

    public <T> Observable<T> toObserverable(Class<T> eventType) {
        return this.bus.ofType(eventType);
    }
}
