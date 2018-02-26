package xyz.eraise.simpleaccountbook.ui.payment.add

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v4.app.Fragment
import xyz.eraise.simpleaccountbook.pojo.Payment
import xyz.eraise.simpleaccountbook.ui.BaseActivity
import xyz.eraise.simpleaccountbook.utils.Constants.Companion.EXTRA_DATA

/**
 * Created by eraise on 2018/2/10.
 */
class AddPaymentActivity: BaseActivity() {

    override fun createFragment(): Fragment = AddPaymentFragment()

    override fun onStart() {
        super.onStart()

        ViewModelProviders
                .of(getFragment())[AddPaymentViewModel::class.java]
                .addPayment
                .observe(this, Observer { onAdd(it!!) })
    }

    private fun onAdd(payment : Payment) {
        setResult(
                RESULT_OK,
                Intent().putExtra(EXTRA_DATA, payment))
        finish()
    }

}