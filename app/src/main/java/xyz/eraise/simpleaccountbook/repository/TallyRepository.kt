package xyz.eraise.simpleaccountbook.repository

import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.rx2.language.RXSQLite
import com.raizlabs.android.dbflow.sql.language.OrderBy
import com.raizlabs.android.dbflow.sql.language.SQLite
import io.reactivex.Maybe
import io.reactivex.Single
import xyz.eraise.simpleaccountbook.pojo.*

/**
 * Created by eraise on 2018/1/22.
 */
object TallyRepository {

    fun saveTally(tally: Tally): Single<Boolean> {
        return tally.save()
    }

    fun getTally(pageIndex: Int = 0, pageSize: Int = 20): Single<List<Tally>> {
        return RXSQLite.rx(
                select from Tally::class
                        where (Tally_Table.is_delete.`is`(false))
                        orderBy OrderBy.fromProperty(Tally_Table.pay_time).descending()
                        offset pageIndex * pageSize
                        limit pageSize
        ).queryList()
    }

}