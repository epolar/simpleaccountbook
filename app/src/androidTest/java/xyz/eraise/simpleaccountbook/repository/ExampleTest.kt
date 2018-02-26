package xyz.eraise.simpleaccountbook.repository

import android.support.test.runner.AndroidJUnit4
import android.test.InstrumentationTestCase
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.SQLite
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import xyz.eraise.simpleaccountbook.pojo.AccountBook
import xyz.eraise.simpleaccountbook.pojo.Payment
import xyz.eraise.simpleaccountbook.pojo.Tally
import xyz.eraise.simpleaccountbook.repository.databases.AppDatabase

/**
 * Created by eraise on 2018/1/22.
 */
@RunWith(AndroidJUnit4::class)
class ExampleTest: InstrumentationTestCase() {

    @Before
    public override fun setUp() {
        super.setUp()
        FlowManager.getDatabase(AppDatabase::class.java).reset()
    }

    @After
    public override fun tearDown() {
        super.tearDown()
        FlowManager.getDatabase(AppDatabase::class.java).reset()
    }

    @Test
    fun test() {
        val book = AccountBook(name = "日常账本")
        book.save().subscribe()
        val selectedAccountBook = SQLite.select()
                .from(AccountBook::class.java)
                .querySingle()
        Assert.assertNotNull(selectedAccountBook)
        Assert.assertEquals(book.name, selectedAccountBook?.name)
        Assert.assertEquals(book.lastModifyTime, selectedAccountBook?.lastModifyTime)
        Assert.assertTrue(selectedAccountBook?.lastModifyTime!! > 0)

        val payment = Payment(name = "支付宝")
        payment.save().subscribe()
        val selectedPayment = SQLite.select()
                .from(Payment::class.java)
                .querySingle()
        Assert.assertNotNull(selectedPayment)
        Assert.assertEquals(payment.name, selectedPayment?.name)

        val tally = Tally()
        tally.accountBook = SQLite.select().from(AccountBook::class.java).querySingle()
        tally.payment = SQLite.select().from(Payment::class.java).querySingle()
        tally.money = 50
        tally.save().subscribe()
        val selectedTally = SQLite.select()
                .from(Tally::class.java)
                .querySingle()
        Assert.assertNotNull(selectedTally)
        Assert.assertNotNull(selectedTally?.accountBook)
        Assert.assertEquals(tally.accountBook?.id, selectedTally?.accountBook?.id)
        Assert.assertNotNull(selectedTally?.payment)
        Assert.assertEquals(tally.payment?.id, selectedTally?.payment?.id)
        Assert.assertEquals(tally.money, selectedTally?.money)
        Assert.assertEquals(tally.lastModifyTime, selectedTally?.lastModifyTime)

        Assert.assertEquals(1, SQLite.select().from(AccountBook::class.java).queryList().size)
        Assert.assertEquals(1, SQLite.select().from(Payment::class.java).queryList().size)
        Assert.assertEquals(1, SQLite.select().from(Tally::class.java).queryList().size)
    }

}