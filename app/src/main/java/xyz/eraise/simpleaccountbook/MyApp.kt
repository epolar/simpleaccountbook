package xyz.eraise.simpleaccountbook

import android.app.Application
import com.dbflow5.config.DatabaseConfig
import com.dbflow5.config.FlowConfig
import com.dbflow5.config.FlowManager
import net.sqlcipher.database.SQLiteDatabase
import xyz.eraise.simpleaccountbook.repository.databases.AppDatabase
import xyz.eraise.simpleaccountbook.repository.databases.SQLCipherHelperImpl
import kotlin.properties.Delegates

/**
 * Created by eraise on 2018/1/19.
 */

class MyApp : Application() {

    companion object {
        var instance: MyApp by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        SQLiteDatabase.loadLibs(this)
        val dbConfig = FlowConfig.Builder(this).database(
            DatabaseConfig
                .Builder(AppDatabase::class) { databaseDefinition, helperListener ->
                    SQLCipherHelperImpl(this, databaseDefinition, helperListener)
                }
                .databaseName("AppDatabase")
                .build()
        ).build()
        FlowManager.init(dbConfig)
    }

    override fun onTerminate() {
        super.onTerminate()

        FlowManager.destroy()
    }

}