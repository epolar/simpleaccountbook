package xyz.eraise.simpleaccountbook.repository

import com.dbflow5.coroutines.defer
import com.dbflow5.query.OrderBy
import com.dbflow5.query.select
import kotlinx.coroutines.Deferred
import xyz.eraise.simpleaccountbook.pojo.Category
import xyz.eraise.simpleaccountbook.pojo.Category_Table

/**
 * Created by eraise on 2018/1/23.
 */
object CategoryRepository : DBProviderImpl() {

    private val emptyCategory = Category()

    suspend fun saveCategory(category: Category): Deferred<Boolean> {
        var c = category
        if (c.isDefault) {
            c = getDefaultCategoryAsync().await() ?: emptyCategory
            if (c != emptyCategory) {
                c.isDefault = false
            }
        }
        return database.beginTransactionAsync {
            c.save(it)
        }.defer()
    }

    fun getCategorys(): Deferred<MutableList<Category>> {
        return (select from Category::class where (Category_Table.is_delete.`is`(false))).async(
            database
        ) { d -> queryList(d) }.defer()
    }

    fun getCategoryCount(): Deferred<Long> {
        return (select from Category::class
                where (Category_Table.is_delete.`is`(false))).async(database) { d -> longValue(d) }
            .defer()

    }

    fun getDefaultCategoryAsync(): Deferred<Category?> {
        return (select from Category::class
                where (Category_Table.is_delete.`is`(false))
                orderBy OrderBy.fromProperty(Category_Table.is_default).descending()
                orderBy OrderBy.fromProperty(Category_Table.last_modify_time).descending()
                )
            .async(database) { d -> querySingle(d) }
            .defer()
    }
}