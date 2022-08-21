package xyz.eraise.simpleaccountbook.repository.databases

import android.content.Context
import com.dbflow5.config.DBFlowDatabase
import com.dbflow5.database.DatabaseCallback
import com.dbflow5.sqlcipher.SQLCipherOpenHelper
import xyz.eraise.simpleaccountbook.MyApp
import xyz.eraise.simpleaccountbook.R

/**
 * Created by eraise on 2018/2/9.
 */
class SQLCipherHelperImpl(
    context: Context,
    databaseDefinition: DBFlowDatabase,
    databaseHelperListener: DatabaseCallback?
) : SQLCipherOpenHelper(context, databaseDefinition, databaseHelperListener) {

    override val cipherSecret
        get() = MyApp.instance.getString(R.string.DB_PASSWORD_APPDATABASE)

}