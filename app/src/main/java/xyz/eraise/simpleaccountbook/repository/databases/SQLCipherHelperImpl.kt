package xyz.eraise.simpleaccountbook.repository.databases

import com.raizlabs.android.dbflow.config.DatabaseDefinition
import com.raizlabs.android.dbflow.sqlcipher.SQLCipherOpenHelper
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener
import xyz.eraise.simpleaccountbook.MyApp
import xyz.eraise.simpleaccountbook.R

/**
 * Created by eraise on 2018/2/9.
 */
class SQLCipherHelperImpl(databaseDefinition: DatabaseDefinition,
                          databaseHelperListener: DatabaseHelperListener?)
    : SQLCipherOpenHelper(databaseDefinition, databaseHelperListener) {

    override fun getCipherSecret(): String =
            MyApp.instance.getString(R.string.DB_PASSWORD_APPDATABASE)

}