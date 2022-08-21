package xyz.eraise.simpleaccountbook.ui.payment.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.eraise.simpleaccountbook.pojo.Payment

/**
 * Created by eraise on 2018/2/24.
 */
class SelectPaymentViewModel : ViewModel() {

    val selected = MutableLiveData<Payment>()

}