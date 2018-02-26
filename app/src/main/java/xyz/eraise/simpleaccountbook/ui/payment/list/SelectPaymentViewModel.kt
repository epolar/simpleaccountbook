package xyz.eraise.simpleaccountbook.ui.payment.list

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import xyz.eraise.simpleaccountbook.pojo.Payment

/**
 * Created by eraise on 2018/2/24.
 */
class SelectPaymentViewModel : ViewModel() {

    val selected = MutableLiveData<Payment>()

}