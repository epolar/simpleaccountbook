package xyz.eraise.simpleaccountbook.ui.payment.add

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import xyz.eraise.simpleaccountbook.pojo.Payment

/**
 * Created by eraise on 2018/2/24.
 */
class AddPaymentViewModel : ViewModel() {

    val addPayment = MutableLiveData<Payment>()

}