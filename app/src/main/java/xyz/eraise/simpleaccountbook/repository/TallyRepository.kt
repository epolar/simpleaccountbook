package xyz.eraise.simpleaccountbook.repository

import com.dbflow5.coroutines.defer
import com.dbflow5.query.Operator
import com.dbflow5.query.OrderBy
import com.dbflow5.query.select
import com.dbflow5.query.sum
import kotlinx.coroutines.Deferred
import xyz.eraise.simpleaccountbook.pojo.Tally
import xyz.eraise.simpleaccountbook.pojo.Tally_Table
import xyz.eraise.simpleaccountbook.utils.DateUtils
import java.util.*

/**
 * Created by eraise on 2018/1/22.
 */
object TallyRepository : DBProviderImpl() {

    fun saveTallyAsync(tally: Tally): Deferred<Boolean> {
        return database.beginTransactionAsync {
            tally.save(it)
        }.defer()
    }

    fun getTallyAsync(pageIndex: Int = 0, pageSize: Int = 20): Deferred<MutableList<Tally>> {
        return (select from Tally::class where (Tally_Table.is_delete.`is`(false))
                orderBy OrderBy.fromProperty(Tally_Table.pay_time).descending()
                orderBy OrderBy.fromProperty(Tally_Table.id).descending()
                offset (pageIndex * pageSize).toLong() limit pageSize.toLong()).async(
            database
        ) { d ->
            queryList(d)
        }.defer()
    }

    fun sumByMonthAsync(year: Int, month: Int): Deferred<Long> {
        val start = Calendar.getInstance()
        start.set(year, month, 1, 0, 0, 0)
        val end = Calendar.getInstance()
        end.set(year, month, 1, 0, 0, 0)
        end.add(Calendar.MONTH, 1)
        return (select(sum(Tally_Table.money)) from Tally::class
                where Tally_Table.pay_time.between(start.timeInMillis).and(end.timeInMillis)).async(
            database
        ) { d -> longValue(d) }
            .defer()
    }
}