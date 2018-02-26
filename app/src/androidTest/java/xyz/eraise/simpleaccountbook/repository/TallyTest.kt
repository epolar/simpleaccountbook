package xyz.eraise.simpleaccountbook.repository

import android.test.InstrumentationTestCase
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.Query
import com.raizlabs.android.dbflow.sql.QueryBuilder
import com.raizlabs.android.dbflow.sql.SqlUtils
import com.raizlabs.android.dbflow.sql.queriable.StringQuery
import io.reactivex.Observable
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test
import xyz.eraise.simpleaccountbook.pojo.AccountBook
import xyz.eraise.simpleaccountbook.pojo.Payment
import xyz.eraise.simpleaccountbook.pojo.Tally
import xyz.eraise.simpleaccountbook.repository.databases.AppDatabase
import java.util.*

/**
 * Created by eraise on 2018/1/23.
 */
class TallyTest: InstrumentationTestCase() {

    private lateinit var mAccountBook: AccountBook
    private lateinit var mPayment: Payment

    @Before
    public override fun setUp() {
        super.setUp()
        FlowManager.getDatabase(AppDatabase::class.java).reset()

        mAccountBook = AccountBook(name = "日常账本")
//        mAccountBook.save().subscribe()
        mPayment = Payment(name = "支付宝")
        mPayment.save().subscribe()
    }

    @After
    public override fun tearDown() {
        super.tearDown()
        FlowManager.getDatabase(AppDatabase::class.java).reset()
    }

    @Test
    fun testTallyList() {
        val random = Random()
        Observable.range(0, 100)
                .flatMapSingle {TallyRepository.saveTally(Tally(accountBook = mAccountBook,
                        payment = mPayment,
                        money = random.nextInt(999_00) + 1_00))}
                .subscribe(
                        {},
                        {},
                        {

                            println("account book id == " + mAccountBook.id)
                            TallyRepository.getTally().subscribe{list -> run {
                                println("account book for list id == " + list[0].accountBook?.id)
                                Assert.assertEquals(20, list.size)
                                Assert.assertEquals(100, list.first().id)}}

                        })
    }

}