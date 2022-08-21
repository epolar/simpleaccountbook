package xyz.eraise.simpleaccountbook.repository.databases

import com.dbflow5.annotation.Database
import com.dbflow5.config.DBFlowDatabase


/**
 * Created by eraise on 2018/1/19.
 */

@Database(version = 2)
abstract class AppDatabase : DBFlowDatabase() {
}