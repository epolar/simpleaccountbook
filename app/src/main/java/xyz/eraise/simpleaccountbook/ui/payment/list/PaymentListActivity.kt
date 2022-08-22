package xyz.eraise.simpleaccountbook.ui.payment.list

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import xyz.eraise.simpleaccountbook.pojo.Payment
import xyz.eraise.simpleaccountbook.ui.BaseActivity
import xyz.eraise.simpleaccountbook.utils.Constants.Companion.EXTRA_DATA

/**
 * Created by eraise on 2018/2/24.
 */
class PaymentListActivity : BaseActivity() {

    override fun createFragment(): Fragment = PaymentListFragment()

    override fun onStart() {
        super.onStart()

        ViewModelProvider(this)[SelectPaymentViewModel::class.java]
            .selected
            .observe(this) { onSelected(it!!) }
    }

    private fun onSelected(payment: Payment) {
        setResult(
            Activity.RESULT_OK,
            Intent().putExtra(EXTRA_DATA, payment)
        )
        finish()
    }

}