package xyz.eraise.simpleaccountbook.repository

import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.orderBy
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import com.raizlabs.android.dbflow.rx2.language.RXSQLite
import com.raizlabs.android.dbflow.sql.language.OrderBy
import io.reactivex.Maybe
import io.reactivex.Single
import xyz.eraise.simpleaccountbook.livedata.DefaultPaymentLiveData
import xyz.eraise.simpleaccountbook.pojo.AccountBook
import xyz.eraise.simpleaccountbook.pojo.Payment
import xyz.eraise.simpleaccountbook.pojo.Payment_Table

/**
 * Created by eraise on 2018/1/23.
 */
object PaymentRepository {

    private val emptyPayment = Payment()

    fun savePayment(payment: Payment): Single<Boolean> {
        return if (payment.isDefault) {
            getDefaultPayment()
                    .switchIfEmpty( Maybe.just(emptyPayment) )
                    .flatMap {
                        if (it != emptyPayment) {
                            it.isDefault = false
                            it.save().toMaybe()
                        } else {
                            Maybe.just(false)
                        }
                    }
                    .flatMapSingle { payment.save() }
                    .doOnSuccess { if (it) DefaultPaymentLiveData.getInstance().postValue(payment) }
        } else {
            payment.save()
        }
    }

    fun getPayments(): Single<List<Payment>> {
        return RXSQLite.rx(
                select from Payment::class
                        where (Payment_Table.is_delete.`is`(false))
        ).queryList()
    }

    fun getPaymentCount(): Single<Long> {
        return RXSQLite.rx(
                select from Payment::class
                        where (Payment_Table.is_delete.`is`(false))
        ).longValue()
    }

    fun getDefaultPayment(): Maybe<Payment> {
        return RXSQLite.rx(
                select from Payment::class
                        where (Payment_Table.is_delete.`is`(false))
                        orderBy OrderBy.fromProperty(Payment_Table.is_default).descending()
                        orderBy OrderBy.fromProperty(Payment_Table.last_modify_time).descending()
        ).querySingle()
    }
}