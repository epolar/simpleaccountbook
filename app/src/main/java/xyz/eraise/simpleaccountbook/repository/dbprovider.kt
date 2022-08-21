package xyz.eraise.simpleaccountbook.repository

import com.dbflow5.config.DBFlowDatabase
import com.dbflow5.config.FlowManager
import xyz.eraise.simpleaccountbook.repository.databases.AppDatabase

interface DBProvider<out T: DBFlowDatabase> {
    val database: T
}

open class DBProviderImpl: DBProvider<AppDatabase> {

    override val database: AppDatabase
        get() = FlowManager.getDatabase(AppDatabase::class.java)

}