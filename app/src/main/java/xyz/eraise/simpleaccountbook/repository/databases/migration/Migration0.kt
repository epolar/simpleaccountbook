package xyz.eraise.simpleaccountbook.repository.databases.migration

import com.dbflow5.annotation.Migration
import com.dbflow5.config.FlowManager
import com.dbflow5.database.DatabaseWrapper
import com.dbflow5.migration.BaseMigration
import com.dbflow5.transaction.FastStoreModelTransaction
import xyz.eraise.simpleaccountbook.MyApp
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.pojo.AccountBook
import xyz.eraise.simpleaccountbook.pojo.Category
import xyz.eraise.simpleaccountbook.pojo.Payment
import xyz.eraise.simpleaccountbook.repository.databases.AppDatabase

/**
 * Created by eraise on 2018/2/26.
 * 初始数据
 */
@Migration(version = 0, database = AppDatabase::class)
class Migration0 : BaseMigration() {
    override fun migrate(database: DatabaseWrapper) {
        initPayments(database)
        initAccountBooks(database)
        initCategories(database)
    }

    private fun initPayments(database: DatabaseWrapper) {
        FastStoreModelTransaction
                .insertBuilder(FlowManager.getModelAdapter(Payment::class.java))
                .addAll(Payment(getString(R.string.alipay)),
                        Payment(getString(R.string.wechat)),
                        Payment(getString(R.string.cash), isDefault = true))
                .build()
                .execute(database)
    }

    private fun initAccountBooks(database: DatabaseWrapper) {
        FastStoreModelTransaction
                .insertBuilder(FlowManager.getModelAdapter(AccountBook::class.java))
                .add(AccountBook(getString(R.string.daily)))
                .build()
                .execute(database)
    }

    private fun initCategories(database: DatabaseWrapper) {
        FastStoreModelTransaction
                .insertBuilder(FlowManager.getModelAdapter(Category::class.java))
                .add(Category(getString(R.string.food)))
                .add(Category(getString(R.string.traffic)))
                .add(Category(getString(R.string.living)))
                .add(Category(getString(R.string.digital)))
                .add(Category(getString(R.string.market)))
                .build()
                .execute(database)
    }

    private fun getString(resId: Int) : String =
            MyApp.instance.getString(resId)

}