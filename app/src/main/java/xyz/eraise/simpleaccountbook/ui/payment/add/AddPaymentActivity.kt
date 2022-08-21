package xyz.eraise.simpleaccountbook.ui.payment.add

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import xyz.eraise.simpleaccountbook.pojo.Payment
import xyz.eraise.simpleaccountbook.ui.BaseActivity
import xyz.eraise.simpleaccountbook.utils.Constants.Companion.EXTRA_DATA

/**
 * Created by eraise on 2018/2/10.
 */
class AddPaymentActivity : BaseActivity() {

    override fun createFragment(): Fragment = AddPaymentFragment()

    override fun onStart() {
        super.onStart()

        ViewModelProvider(this)[AddPaymentViewModel::class.java]
            .addPayment
            .observe(this) { onAdd(it!!) }
    }

    private fun onAdd(payment: Payment) {
        setResult(
            RESULT_OK,
            Intent().putExtra(EXTRA_DATA, payment)
        )
        finish()
    }

}