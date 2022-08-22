package xyz.eraise.simpleaccountbook.repository

import android.test.InstrumentationTestCase
import com.raizlabs.android.dbflow.config.FlowManager
import io.reactivex.Observable
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test
import xyz.eraise.simpleaccountbook.pojo.AccountBook
import xyz.eraise.simpleaccountbook.repository.databases.AppDatabase

/**
 * Created by eraise on 2018/1/23.
 */
class AccountBookTest: InstrumentationTestCase() {

    private lateinit var company: AccountBook
    private lateinit var daily: AccountBook
    private lateinit var journey: AccountBook

    @Before
    public override fun setUp() {
        super.setUp()

        FlowManager.getDatabase(AppDatabase::class.java).reset()

        daily = AccountBook(name = "日常")
        journey = AccountBook(name = "旅途", isDefault = true)
        company = AccountBook(name = "公司", isDefault = true)
        company.isDelete = true
        Observable.just(daily, journey, company)
                .flatMapSingle {book -> AccountBookRepository.saveAccountBookAsync(book) }
                .subscribe()
    }

    @After
    public override fun tearDown() {
        super.tearDown()

        FlowManager.getDatabase(AppDatabase::class.java).reset()
    }

    @Test
    fun testSetDefaultAccountBook() {
        daily.isDefault = true
        AccountBookRepository.saveAccountBookAsync(daily)
                .flatMapMaybe { AccountBookRepository.getDefaultAccountBookAsync()}
                .subscribe({
                    println("set default account book is " + it.name)
                    Assert.assertEquals(it.name, "日常")
                }, {println("set default account error")}, {
                    println("set default account completed")
                })

    }

    @Test
    fun testSetDefaultAccountBook2() {
        daily.isDefault = true
        AccountBookRepository.saveAccountBookAsync(daily)
                .map { journey.isDefault = true }
                .flatMap { AccountBookRepository.saveAccountBookAsync(journey) }
                .map { daily.isDefault = true }
                .flatMap { AccountBookRepository.saveAccountBookAsync(daily) }
                .flatMapMaybe { AccountBookRepository.getDefaultAccountBookAsync()}
                .subscribe({
                    println("set default account 2 book is " + it.name)
                    Assert.assertEquals(it.name, daily.name)
                }, {println("set default account 2 error")}, {
                    println("set default account 2 completed")
                })

    }

    @Test
    fun testDefaultAccountBook() {
        AccountBookRepository.getDefaultAccountBookAsync()
                .subscribe({
            println("default account book is " + it.name)
            Assert.assertEquals(it.name, "旅途")
        }, {println("default account error")}, {
            println("default account completed")
        })
    }

}