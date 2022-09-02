package xyz.eraise.simpleaccountbook.ui.tally.add

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.databinding.FragmentAddTallyBinding
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
import xyz.eraise.simpleaccountbook.utils.kotlin.isToday
import java.math.BigDecimal
import java.text.MessageFormat
import java.util.*
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

    private var _binding: FragmentAddTallyBinding? = null
    private val binding get() = _binding!!

    private var mPayment: Payment? = null
    private var mAccountBook: AccountBook? = null
    private var mCategory: Category? = null
    private val mDate = Calendar.getInstance()

    private val mNumbers: ArrayList<String> = ArrayList()
    private val mOperations: ArrayList<String> = ArrayList()

    private var mAmount: BigDecimal = BigDecimal.valueOf(0)

    private val mInputPlateAdapter = InputPlateAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTallyBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 事件绑定
        binding.btnPayment.setOnClickListener {
            startActivityForResult(
                Intent(context, PaymentListActivity::class.java),
                REQUEST_PAYMENT
            )
        }
        binding.btnAccountBook.setOnClickListener {
            startActivityForResult(
                Intent(context, AccountBookListActivity::class.java),
                REQUEST_ACCOUNT_BOOK
            )
        }
        binding.btnCategory.setOnClickListener {
            startActivityForResult(
                Intent(context, CategoryListActivity::class.java),
                REQUEST_CATEGORY
            )
        }
        binding.btnDate.setOnClickListener { pickDate() }
        binding.btnList.setOnClickListener {
            startActivity(Intent(context, TallyListActivity::class.java))
        }

        binding.etProject.setOnEditorActionListener { _, _, _ ->
            save()
            true
        }

        initInputPlate()

        // 设置默认值显示
        initDefault()
    }

    override fun onResume() {
        super.onResume()

        refreshThisMonth()
    }

    private fun refreshThisMonth() {
        lifecycleScope.launch {
            val now = Calendar.getInstance()
            TallyRepository.sumByMonthAsync(now[Calendar.YEAR], now[Calendar.MONTH])
                .await()
                .let {
                    binding.tvThisMonth.text =
                        getString(
                            R.string.add_tally_this_month, it / 100.0
                        )
                }
        }
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
    @OptIn(DelicateCoroutinesApi::class)
    private fun initDefault() {
        lifecycleScope.launch {
            PaymentRepository
                .getDefaultPaymentAsync()
                .await().let { setPayment(it) }
            AccountBookRepository
                .getDefaultAccountBookAsync()
                .await().let { setAccountBook(it) }
            CategoryRepository
                .getDefaultCategoryAsync()
                .await().let { setCategory(it) }
        }
    }

    /**
     * 初始化数字输入控件
     */
    private fun initInputPlate() {
        mInputPlateAdapter.setOnItemClickListener { _, _, position ->
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

        binding.rvInputPlate.layoutManager = GridLayoutManager(context, 4)
        binding.rvInputPlate.adapter = mInputPlateAdapter
    }

    /**
     * 用户按输入控件
     */
    private fun onClickInput(position: Int) {
        when (position) {
            0, 1, 2, 4, 5, 6, 8, 9, 10, 13 ->
                onInputNumber(mInputPlateAdapter.getItem(position))
            14 -> onInputDot()
            3 -> onInputBack()
            7 -> onInputOperate("+")
            11 -> onInputOperate("-")
            15 -> save()
        }
    }

    private fun isLastInputIsOperation(): Boolean {
        if (binding.tvEquation.text.isEmpty()) {
            return false
        }
        val lastInput = binding.tvEquation.text.last().toString()
        return lastInput == "+" || lastInput == "-"
    }

    private fun str2BigDecimal(numberStr: String): BigDecimal {
        return if (numberStr.endsWith(".")) {
            BigDecimal(
                numberStr.substring(0, numberStr.length - 1)
            )
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
        binding.tvEquation.append(operate)
        binding.tvEquation.visibility = View.VISIBLE
        mOperations.add(operate)
    }

    /**
     * 按小数点
     */
    private fun onInputDot() {
        when {
            isLastInputIsOperation() -> {
                mNumbers.add("0.")
                binding.tvEquation.append("0.")
            }
            mNumbers.last().contains(".") -> return
            else -> {
                var lastNumber = mNumbers.removeAt(mNumbers.size - 1)
                lastNumber += "."
                mNumbers.add(lastNumber)
                binding.tvEquation.append(".")
            }
        }
    }

    /**
     * 按删除
     */
    private fun onInputBack() {
        if (binding.tvEquation.text.length <= 1) {
            binding.etMoney.text = ""
            binding.tvEquation.text = ""
            mNumbers.clear()
            mOperations.clear()
            mAmount = BigDecimal.ZERO
            binding.tvEquation.visibility = View.GONE
        } else {
            val deleteChar = binding.tvEquation.text.last().toString()
            binding.tvEquation.text = binding.tvEquation.text
                .subSequence(0, binding.tvEquation.text.length - 1)
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
            binding.etMoney.text = MessageFormat.format(
                "{0, number, #.##}",
                mAmount.toDouble()
            )
        }
    }

    /**
     * 按数字
     */
    private fun onInputNumber(numberStr: CharSequence) {
        var lastNumber =
            if (binding.tvEquation.text.isEmpty() || isLastInputIsOperation())
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

        binding.tvEquation.append(numberStr)
        binding.etMoney.text = MessageFormat.format(
            "{0, number, #.##}",
            mAmount.toDouble()
        )
    }

    private fun pickDate() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                pickTime(year, month, dayOfMonth)
            },
            mDate[Calendar.YEAR],
            mDate[Calendar.MONTH],
            mDate[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun pickTime(year: Int, month: Int, dayOfMonth: Int) {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                setDate(year, month, dayOfMonth, hour, minute)
            },
            mDate.get(Calendar.HOUR_OF_DAY),
            mDate.get(Calendar.MINUTE),
            true,
        )
        timePickerDialog.show()
    }

    private fun setPayment(payment: Payment?) {
        mPayment = payment
        binding.btnPayment.text = payment?.name ?: getString(R.string.add_tally_payment)
    }

    private fun setAccountBook(accountBook: AccountBook?) {
        mAccountBook = accountBook
        binding.btnAccountBook.text =
            accountBook?.name ?: getString(R.string.add_tally_account_book)
    }

    private fun setCategory(category: Category?) {
        mCategory = category
        binding.btnCategory.text = category?.name ?: getString(R.string.add_tally_category)
    }

    private fun setDate(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int) {
        mDate.set(Calendar.YEAR, year)
        mDate.set(Calendar.MONTH, month)
        mDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        mDate.set(Calendar.HOUR, hour)
        mDate.set(Calendar.MINUTE, minute)
        if (mDate.isToday()) {
            binding.btnDate.setText(R.string.add_tally_today)
        } else {
            binding.btnDate.text =
                DateUtils.timestampToDateStr(mDate.timeInMillis)
        }
    }

    private fun save() {
        if (binding.tvEquation.text.isEmpty()) {
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
            project = binding.etProject.text.toString(),
            money = money,
            payTime = mDate.timeInMillis
        )
        TallyRepository
            .saveTallyAsync(tally)
            .let { onSaveSuccess() }
    }

    private fun onSaveSuccess() {
        PromptUtils.toast(R.string.add_tally_save_success)
        mAmount = BigDecimal.ZERO
        binding.tvEquation.requestFocus()
        binding.tvEquation.text = ""
        binding.etMoney.text = ""
        binding.etProject.text.clear()
        mNumbers.clear()
        mOperations.clear()

        refreshThisMonth()
    }

}