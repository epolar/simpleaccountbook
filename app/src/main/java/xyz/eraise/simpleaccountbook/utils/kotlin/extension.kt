package xyz.eraise.simpleaccountbook.utils.kotlin

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*

/*
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

/**
 * Calendar当前保存的日期是否为今天
 */
fun Calendar.isToday(): Boolean {
    val now = Calendar.getInstance()
    return  this[Calendar.YEAR] == now[Calendar.YEAR]
            && this[Calendar.MONTH] == now[Calendar.MONTH]
            && this[Calendar.DAY_OF_MONTH] == now[Calendar.DAY_OF_MONTH]
}