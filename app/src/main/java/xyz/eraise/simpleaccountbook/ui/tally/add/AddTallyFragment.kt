package xyz.eraise.simpleaccountbook.ui.tally.add

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
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
import xyz.eraise.simpleaccountbook.utils.kotlin.isToday
import java.math.BigDecimal
import java.text.MessageFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

/**
 * Created by eraise on 2018/2/10.
 */
class AddTallyFragment : Fragment() {

    companion object {
        private const val REQUEST_PAYMENT = 1
        private const val REQUEST_ACCOUNT_BOOK = 2
        private const val REQUEST_CATEGORY = 3
    }

    private var mPayment: Payment? = null
    private var mAccountBook: AccountBook? = null
    private var mCategory: Category? = null
    private val mDate = Calendar.getInstance()

    private val mNumbers: ArrayList<String> = ArrayList()
    private val mOperations: ArrayList<String> = ArrayList()

    private var mAmount: BigDecimal = BigDecimal.valueOf(0)

    private val mInputPlateAdapter = InputPlateAdapter()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.fragment_add_tally,
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
        btn_list.setOnClickListener {
            startActivity(Intent(context, TallyListActivity::class.java))
        }

        initInputPlate()

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

    /**
     * 初始化数字输入控件
     */
    private fun initInputPlate() {
        mInputPlateAdapter.onItemClickListener =
                BaseQuickAdapter.OnItemClickListener { _, _, position ->
                    onClickInput(position)
                }

        val numbers = resources.getStringArray(R.array.add_tally_input_numbers)
        // 第一排
        for (i in 0 until 3) {
            mInputPlateAdapter.addData(numbers[i])
        }
        mInputPlateAdapter.addDrawableItem(R.drawable.ic_delete)
        // 第二排
        for (i in 3 until 6) {
            mInputPlateAdapter.addData(numbers[i])
        }
        mInputPlateAdapter.addData("+")
        // 第三排
        for (i in 6 until 9) {
            mInputPlateAdapter.addData(numbers[i])
        }
        mInputPlateAdapter.addData("-")
        // 第四排
        mInputPlateAdapter.addData(" ")
        mInputPlateAdapter.addData(numbers[9])
        mInputPlateAdapter.addData(numbers[10])
        mInputPlateAdapter.addData(numbers[11])

        rv_input_plate.layoutManager = GridLayoutManager(context, 4)
        rv_input_plate.adapter = mInputPlateAdapter
    }

    /**
     * 用户按输入控件
     */
    private fun onClickInput(position: Int) {
        when (position) {
            0, 1, 2, 4, 5, 6, 8, 9, 10, 13 ->
                onInputNumber(mInputPlateAdapter.getItem(position)!!)
            14 -> onInputDot()
            3 -> onInputBack()
            7 -> onInputOperate("+")
            11 -> onInputOperate("-")
            15 -> save()
        }
    }

    private fun isLastInputIsOperation(): Boolean {
        if (tv_equation.text.isEmpty()) {
            return false
        }
        val lastInput = tv_equation.text.last().toString()
        return lastInput == "+" || lastInput == "-"
    }

    private fun str2BigDecimal(numberStr: String): BigDecimal {
        return if (numberStr.endsWith(".")) {
            BigDecimal(
                    numberStr.substring(0, numberStr.length - 1))
        } else {
            BigDecimal(numberStr)
        }
    }

    private fun getLastInputOperation(): String {
        return if (mOperations.isEmpty())
            "+"
        else
            mOperations.last()
    }

    /**
     * 按运算符号
     */
    private fun onInputOperate(operate: String) {
        if (isLastInputIsOperation()) {
            // 上一次输入的也是运算符号，清除掉
            onInputBack()
        }
        tv_equation.append(operate)
        tv_equation.visibility = View.VISIBLE
        mOperations.add(operate)
    }

    /**
     * 按小数点
     */
    private fun onInputDot() {
        when {
            isLastInputIsOperation() -> {
                mNumbers.add("0.")
                tv_equation.append("0.")
            }
            mNumbers.last().contains(".") -> return
            else -> {
                var lastNumber = mNumbers.removeAt(mNumbers.size - 1)
                lastNumber += "."
                mNumbers.add(lastNumber)
                tv_equation.append(".")
            }
        }
    }

    /**
     * 按删除
     */
    private fun onInputBack() {
        if (tv_equation.text.length <= 1) {
            et_money.text = ""
            tv_equation.text = ""
            mNumbers.clear()
            mOperations.clear()
            mAmount = BigDecimal.ZERO
            tv_equation.visibility = View.GONE
        } else {
            val deleteChar = tv_equation.text.last().toString()
            tv_equation.text = tv_equation.text
                    .subSequence(0, tv_equation.text.length - 1)
            if (deleteChar == "+" || deleteChar == "-") {
                // 符号不会影响到数字
                mOperations.removeAt(mOperations.size - 1)
            } else if (deleteChar == ".") {
                // 小数点不会影响到数字
                val lastNumber = mNumbers.removeAt(mNumbers.size - 1)
                mNumbers.add(lastNumber.substring(0, lastNumber.length - 1))
            } else {
                var lastNumber = mNumbers.removeAt(mNumbers.size - 1)
                // 对上一个运算符做反操作
                mAmount = if (getLastInputOperation() == "+")
                    mAmount.subtract(str2BigDecimal(lastNumber))
                else
                    mAmount.add(str2BigDecimal(lastNumber))
                // 重新进行运算
                if (lastNumber.length > 1) {
                    lastNumber = lastNumber.substring(0, lastNumber.length - 1)
                    mAmount = if (getLastInputOperation() == "+")
                        mAmount.add(str2BigDecimal(lastNumber))
                    else
                        mAmount.subtract(str2BigDecimal(lastNumber))
                    mNumbers.add(lastNumber)
                }
            }
            et_money.text = MessageFormat.format(
                    "{0, number, #.##}",
                    mAmount.toDouble())
        }
    }

    /**
     * 按数字
     */
    private fun onInputNumber(numberStr: CharSequence) {
        var lastNumber =
                if (tv_equation.text.isEmpty() || isLastInputIsOperation())
                    ""
                else
                    mNumbers.removeAt(mNumbers.size - 1)
        if (lastNumber.isNotEmpty()) {
            mAmount = mAmount.subtract(str2BigDecimal(lastNumber))
        }
        lastNumber += numberStr
        mAmount = if (getLastInputOperation() == "+") {
            mAmount.add(str2BigDecimal(lastNumber))
        } else {
            mAmount.subtract(str2BigDecimal(lastNumber))
        }
        mNumbers.add(lastNumber)

        tv_equation.append(numberStr)
        et_money.text = MessageFormat.format(
                "{0, number, #.##}",
                mAmount.toDouble())
    }

    private fun pickDate() {
        val datePickerDialog = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    setDate(year, month, dayOfMonth)
                },
                mDate[Calendar.YEAR],
                mDate[Calendar.MONTH],
                mDate[Calendar.DAY_OF_MONTH])
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
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

    private fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        mDate.set(Calendar.YEAR, year)
        mDate.set(Calendar.MONTH, month)
        mDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        if (mDate.isToday()) {
            btn_date.setText(R.string.add_tally_today)
        } else {
            btn_date.text =
                    DateUtils.timestampToDateStr(mDate.timeInMillis)
        }
    }

    private fun save() {
        if (tv_equation.text.isEmpty()) {
            PromptUtils.toast(R.string.add_tally_money_should_not_be_empty)
            return
        }

        // 四舍五入后转到分为单位
        val money = mAmount.multiply(BigDecimal.valueOf(100))
                .toDouble()
                .roundToInt()

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
                .subscribe(Consumer { onSaveSuccess() })
    }

    private fun onSaveSuccess() {
        PromptUtils.toast(R.string.add_tally_save_success)
        mAmount = BigDecimal.ZERO
        tv_equation.requestFocus()
        tv_equation.text = ""
        et_money.text = ""
        et_project.text.clear()
        mNumbers.clear()
        mOperations.clear()
    }

}