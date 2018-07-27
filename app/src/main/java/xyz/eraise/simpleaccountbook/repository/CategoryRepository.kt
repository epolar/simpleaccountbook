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
import xyz.eraise.simpleaccountbook.pojo.Category
import xyz.eraise.simpleaccountbook.pojo.Category_Table
import xyz.eraise.simpleaccountbook.repository.PaymentRepository.getDefaultPayment

/**
 * Created by eraise on 2018/1/23.
 */
object CategoryRepository {

    private val emptyCategory = Category()

    fun saveCategory(category: Category): Single<Boolean> {
        return if (category.isDefault) {
            getDefaultCategory()
                    .switchIfEmpty( Maybe.just(emptyCategory) )
                    .flatMap {
                        if (it != emptyCategory) {
                            it.isDefault = false
                            it.save().toMaybe()
                        } else {
                            Maybe.just(false)
                        }
                    }
                    .flatMapSingle { category.save() }
        } else {
            category.save()
        }
    }

    fun getCategorys(): Single<List<Category>> {
        return RXSQLite.rx(
                select from Category::class
                        where (Category_Table.is_delete.`is`(false))
        ).queryList()
    }

    fun getCategoryCount(): Single<Long> {
        return RXSQLite.rx(
                select from Category::class
                        where (Category_Table.is_delete.`is`(false))
        ).longValue()
    }

    fun getDefaultCategory(): Maybe<Category> {
        return RXSQLite.rx(
                select from Category::class
                        where (Category_Table.is_delete.`is`(false))
                        orderBy OrderBy.fromProperty(Category_Table.is_default).descending()
                        orderBy OrderBy.fromProperty(Category_Table.last_modify_time).descending()
        ).querySingle()
    }
}