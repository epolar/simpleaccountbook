package xyz.eraise.simpleaccountbook.repository.databases

import com.raizlabs.android.dbflow.annotation.Database

/**
 * Created by eraise on 2018/1/19.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
object AppDatabase {

    const val NAME = "AppDatabase"
    const val VERSION  = 1

}