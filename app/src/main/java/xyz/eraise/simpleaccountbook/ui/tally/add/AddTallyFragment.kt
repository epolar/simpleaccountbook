package xyz.eraise.simpleaccountbook.ui.tally.add

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_add_tally.*
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.pojo.AccountBook
import xyz.eraise.simpleaccountbook.pojo.Category
import xyz.eraise.simpleaccountbook.pojo.Payment
import xyz.eraise.simpleaccountbook.pojo.Tally
import xyz.eraise.simpleaccountbook.repository.AccountBookRepository
import xyz.eraise.simpleaccountbook.repository.CategoryRepository
import xyz.eraise.simpleaccountbook.repository.PaymentRepository
import xyz.eraise.simpleaccountbook.repository.TallyRepository
import xyz.eraise.simpleaccountbook.ui.accountbook.list.AccountBookListActivity
import xyz.eraise.simpleaccountbook.ui.category.list.CategoryListActivity
import xyz.eraise.simpleaccountbook.ui.payment.list.PaymentListActivity
import xyz.eraise.simpleaccountbook.ui.tally.list.TallyListActivity
import xyz.eraise.simpleaccountbook.utils.Constants.Companion.EXTRA_DATA
import xyz.eraise.simpleaccountbook.utils.DateUtils
import xyz.eraise.simpleaccountbook.utils.PromptUtils
import xyz.eraise.simpleaccountbook.utils.kotlin.async
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by eraise on 2018/2/10.
 */
class AddTallyFragment: Fragment() {

    companion object {
        private const val REQUEST_PAYMENT = 1
        private const val REQUEST_ACCOUNT_BOOK = 2
        private const val REQUEST_CATEGORY = 3
    }

    private var mPayment: Payment? = null
    private var mAccountBook: AccountBook? = null
    private var mCategory: Category? = null
    private val mDate = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View?
            = inflater.inflate(R.layout.fragment_add_tally,
            container,
            false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 事件绑定
        btn_payment.setOnClickListener {
            startActivityForResult(
                    Intent(context, PaymentListActivity::class.java),
                    REQUEST_PAYMENT)
        }
        btn_account_book.setOnClickListener {
            startActivityForResult(
                    Intent(context, AccountBookListActivity::class.java),
                    REQUEST_ACCOUNT_BOOK)
        }
        btn_category.setOnClickListener {
            startActivityForResult(
                    Intent(context, CategoryListActivity::class.java),
                    REQUEST_CATEGORY)
        }
        btn_date.setOnClickListener { pickDate() }
        btn_save.setOnClickListener { save() }
        btn_list.setOnClickListener {
            startActivity(Intent(context, TallyListActivity::class.java))
        }

        // 设置默认值显示
        initDefault()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            // 不需要做任何操作
            return
        }

        when (requestCode) {
            REQUEST_ACCOUNT_BOOK ->
                setAccountBook(data?.getParcelableExtra(EXTRA_DATA))
            REQUEST_CATEGORY ->
                setCategory(data?.getParcelableExtra(EXTRA_DATA))
            REQUEST_PAYMENT ->
                setPayment(data?.getParcelableExtra(EXTRA_DATA))
        }
    }

    /**
     * 设置默认值显示
     */
    private fun initDefault() {
        // 去掉不需要的具体时间
        val year = mDate[Calendar.YEAR]
        val month = mDate[Calendar.MONTH]
        val dayOfMonth = mDate[Calendar.DAY_OF_MONTH]
        mDate.set(year, month, dayOfMonth, 0, 0, 0)
        btn_date.text = DateUtils.timestampToDateStr(mDate.timeInMillis)

        PaymentRepository
                .getDefaultPayment()
                .async()
                .subscribe { setPayment(it) }
        AccountBookRepository
                .getDefaultAccountBook()
                .async()
                .subscribe { setAccountBook(it) }
        CategoryRepository
                .getDefaultCategory()
                .async()
                .subscribe { setCategory(it) }
    }

    private fun pickDate() {
        DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    mDate.set(Calendar.YEAR, year)
                    mDate.set(Calendar.MONTH, month)
                    mDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    btn_date.text =
                            DateUtils.timestampToDateStr(mDate.timeInMillis)
                },
                mDate[Calendar.YEAR],
                mDate[Calendar.MONTH],
                mDate[Calendar.DAY_OF_MONTH])
                .show()
    }

    private fun setPayment(payment: Payment?) {
        mPayment = payment
        btn_payment.text = payment?.name ?: getString(R.string.add_tally_payment)
    }

    private fun setAccountBook(accountBook: AccountBook?) {
        mAccountBook = accountBook
        btn_account_book.text = accountBook?.name ?: getString(R.string.add_tally_account_book)
    }

    private fun setCategory(category: Category?) {
        mCategory = category
        btn_category.text = category?.name ?: getString(R.string.add_tally_category)
    }

    private fun save() {
        if (et_money.text.isEmpty()) {
            PromptUtils.toast(R.string.add_tally_money_should_not_be_empty)
            return
        }
        // 元为单位
        val moneyStr = et_money.text.toString()
        // 四舍五入后转到分为单位
        val money = (moneyStr.toDouble() * 100).roundToInt()

        if (mPayment == null) {
            PromptUtils.toast(R.string.add_tally_payment_should_selected)
            return
        }
        if (mAccountBook == null) {
            PromptUtils.toast(R.string.add_tally_account_book_should_selected)
            return
        }
        if (mCategory == null) {
            PromptUtils.toast(R.string.add_tally_category_should_selected)
            return
        }
        val tally = Tally(
                accountBook = mAccountBook,
                payment = mPayment,
                category = mCategory,
                project = et_project.text.toString(),
                money = money,
                payTime = mDate.timeInMillis)
        TallyRepository
                .saveTally(tally)
                .subscribe ( Consumer { PromptUtils.toast("保存成功") } )
    }

}