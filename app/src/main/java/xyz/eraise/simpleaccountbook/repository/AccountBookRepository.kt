package xyz.eraise.simpleaccountbook.repository

import com.dbflow5.coroutines.defer
import com.dbflow5.query.OrderBy
import com.dbflow5.query.select
import kotlinx.coroutines.Deferred
import xyz.eraise.simpleaccountbook.pojo.AccountBook
import xyz.eraise.simpleaccountbook.pojo.AccountBook_Table

/**
 * Created by eraise on 2018/1/23.
 */
object AccountBookRepository : DBProviderImpl() {

    private val emptyAccountBook = AccountBook()

    suspend fun saveAccountBook(accountBook: AccountBook): Deferred<Boolean> {
        var ab = accountBook
        if (accountBook.isDefault) {
            ab = getDefaultAccountBookAsync().await() ?: emptyAccountBook
            if (ab != emptyAccountBook) {
                ab.isDefault = false
            }
        }
        return database.beginTransactionAsync {
            ab.save(it)
        }.defer()
    }

    fun getAccountBooks(): Deferred<List<AccountBook>> {
        return (select from AccountBook::class where (AccountBook_Table.is_delete.`is`(false))).async(
            database
        ) { d ->
            queryList(
                d
            )
        }.defer()
    }

    fun getAccountBookCount(): Deferred<Long> {
        return (select from AccountBook::class
                where (AccountBook_Table.is_delete.`is`(false))
                ).async(database) { d -> longValue(d) }.defer()

    }

    fun getDefaultAccountBookAsync(): Deferred<AccountBook?> {
        return (select from AccountBook::class
                where (AccountBook_Table.is_delete.`is`(false))
                orderBy OrderBy.fromProperty(AccountBook_Table.is_default).descending()
                orderBy OrderBy.fromProperty(AccountBook_Table.last_modify_time).descending())
            .async(database) { d -> querySingle(d) }
            .defer()
    }

}