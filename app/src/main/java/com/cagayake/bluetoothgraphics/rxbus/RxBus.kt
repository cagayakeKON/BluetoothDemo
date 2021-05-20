package com.cagayake.bluetoothgraphics.rxbus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject



object RxBus {
   private var mBus: Subject<Any> = PublishSubject.create<Any>().toSerialized();

    fun post(event:Any):Unit{
        mBus.onNext(event)
    }

    fun <T>toObservable(tClass:Class<T>): Observable<T> {
        return mBus.ofType(tClass)
    }
}