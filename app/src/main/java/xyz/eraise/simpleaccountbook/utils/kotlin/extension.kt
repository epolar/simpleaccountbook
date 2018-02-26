package xyz.eraise.simpleaccountbook.utils.kotlin

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Created by eraise on 2018/2/2.
 * kotlin 的一些拓展
 */
fun Completable.async():Completable =
        this.observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

fun <T> Flowable<T>.async():Flowable<T> =
        this.observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())

fun <T> Maybe<T>.async():Maybe<T> =
        this.observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())

fun <T> Single<T>.async():Single<T> =
        this.observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())