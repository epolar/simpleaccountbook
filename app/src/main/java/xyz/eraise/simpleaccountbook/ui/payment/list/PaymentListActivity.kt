package xyz.eraise.simpleaccountbook.ui.payment.list

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v4.app.Fragment
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

        ViewModelProviders
                .of(getFragment())[SelectPaymentViewModel::class.java]
                .selected
                .observe(this, Observer { onSelected(it!!) })
    }

    private fun onSelected(payment : Payment) {
        setResult(
                Activity.RESULT_OK,
                Intent().putExtra(EXTRA_DATA, payment))
        finish()
    }

}