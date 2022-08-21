package xyz.eraise.simpleaccountbook.repository

import com.dbflow5.coroutines.defer
import com.dbflow5.query.OrderBy
import com.dbflow5.query.select
import kotlinx.coroutines.Deferred
import xyz.eraise.simpleaccountbook.pojo.Payment
import xyz.eraise.simpleaccountbook.pojo.Payment_Table

/**
 * Created by eraise on 2018/1/23.
 */
object PaymentRepository : DBProviderImpl() {

    private val emptyPayment = Payment()

    suspend fun savePayment(p: Payment): Deferred<Boolean> {
        var payment = p
        if (payment.isDefault) {
            payment = getDefaultPaymentAsync().await() ?: emptyPayment
            if (payment != emptyPayment) {
                payment.isDefault = false
            }
        }
        return database.beginTransactionAsync {
            payment.save(it)
        }.defer()
    }

    fun getPayments(): Deferred<MutableList<Payment>> {
        return (select from Payment::class
                where (Payment_Table.is_delete.`is`(false))
                ).async(database) { d -> queryList(d) }.defer()
    }

    fun getPaymentCount(): Deferred<Long> {
        return (select from Payment::class
                where (Payment_Table.is_delete.`is`(false)))
            .async(database) { d -> longValue(d) }.defer()
    }

    fun getDefaultPaymentAsync(): Deferred<Payment?> {
        return (select from Payment::class
                where (Payment_Table.is_delete.`is`(false))
                orderBy OrderBy.fromProperty(Payment_Table.is_default).descending()
                orderBy OrderBy.fromProperty(Payment_Table.last_modify_time).descending()
                ).async(database) { d -> querySingle(d) }
            .defer()
    }

    fun getDefaultPayment(): Payment? {
        return (select from Payment::class
                where (Payment_Table.is_delete.`is`(false))
                orderBy OrderBy.fromProperty(Payment_Table.is_default).descending()
                orderBy OrderBy.fromProperty(Payment_Table.last_modify_time).descending()
                ).querySingle(database)
    }
}