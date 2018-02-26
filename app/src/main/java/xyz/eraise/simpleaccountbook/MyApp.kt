package xyz.eraise.simpleaccountbook

import android.app.Application
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import net.sqlcipher.database.SQLiteDatabase
import xyz.eraise.simpleaccountbook.repository.databases.AppDatabase
import xyz.eraise.simpleaccountbook.repository.databases.SQLCipherHelperImpl
import kotlin.properties.Delegates

/**
 * Created by eraise on 2018/1/19.
 */

class MyApp : Application() {

    companion object {
        var instance : MyApp by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        SQLiteDatabase.loadLibs(this)
        val dbConfig = FlowConfig.Builder(this)
                .addDatabaseConfig(DatabaseConfig
                        .Builder(AppDatabase::class.java)
                        .openHelper { databaseDefinition, helperListener ->
                            SQLCipherHelperImpl(databaseDefinition, helperListener) }
                        .build())
                .build()
        FlowManager.init(dbConfig)
    }

    override fun onTerminate() {
        super.onTerminate()

        FlowManager.destroy()
    }

}