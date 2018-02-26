package xyz.eraise.simpleaccountbook.repository

import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.orderBy
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import com.raizlabs.android.dbflow.rx2.language.RXSQLite
import com.raizlabs.android.dbflow.sql.language.OrderBy
import io.reactivex.Maybe
import io.reactivex.Single
import xyz.eraise.simpleaccountbook.pojo.AccountBook
import xyz.eraise.simpleaccountbook.pojo.AccountBook_Table

/**
 * Created by eraise on 2018/1/23.
 */
object AccountBookRepository {

    private val emptyAccountBook = AccountBook()

    fun saveAccountBook(accountBook: AccountBook): Single<Boolean> {
        return if (accountBook.isDefault) {
            getDefaultAccountBook()
                    .switchIfEmpty( Maybe.just(emptyAccountBook) )
                    .flatMap {
                        if (it != emptyAccountBook) {
                            it.isDefault = false
                            it.save().toMaybe()
                        } else {
                            Maybe.just(false)
                        }
                    }
                    .flatMapSingle { accountBook.save() }
        } else {
            accountBook.save()
        }
    }

    fun getAccountBooks(): Single<List<AccountBook>> {
        return RXSQLite.rx(
                select from AccountBook::class
                        where (AccountBook_Table.is_delete.`is`(false))
        ).queryList()
    }

    fun getAccountBookCount(): Single<Long> {
        return RXSQLite.rx(
                select from AccountBook::class
                        where (AccountBook_Table.is_delete.`is`(false))
        ).longValue()
    }

    fun getDefaultAccountBook(): Maybe<AccountBook> {
        return RXSQLite.rx(
                select from AccountBook::class
                        where (AccountBook_Table.is_delete.`is`(false))
                        orderBy OrderBy.fromProperty(AccountBook_Table.is_default).descending()
                        orderBy OrderBy.fromProperty(AccountBook_Table.last_modify_time).descending()
        ).querySingle()
    }
}