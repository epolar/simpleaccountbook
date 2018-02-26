package xyz.eraise.simpleaccountbook.ui.payment.add

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_add_payment.*
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.pojo.Payment
import xyz.eraise.simpleaccountbook.repository.PaymentRepository
import xyz.eraise.simpleaccountbook.utils.PromptUtils

/**
 * Created by eraise on 2018/2/10.
 */
class AddPaymentFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View?
            = inflater.inflate(
            R.layout.fragment_add_payment, container,
            false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_save.setOnClickListener { save() }
    }

    private fun save() {
        val name = et_payment_name.text.toString()
        if (name.isEmpty()) {
            PromptUtils.toast(R.string.add_payment_name_should_not_be_empty)
            return
        }
        val payment = Payment(name)
        PaymentRepository
                .savePayment(payment)
                .subscribe( Consumer { if (it) onSaveSuccess(payment) } )
    }

    private fun onSaveSuccess(payment : Payment) {
        ViewModelProviders
                .of(this)[AddPaymentViewModel::class.java]
                .addPayment
                .postValue(payment)
    }

}