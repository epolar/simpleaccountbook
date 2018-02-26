package xyz.eraise.simpleaccountbook.livedata

import android.arch.lifecycle.MutableLiveData
import xyz.eraise.simpleaccountbook.pojo.Payment
import xyz.eraise.simpleaccountbook.repository.PaymentRepository
import xyz.eraise.simpleaccountbook.utils.SingletonHolderNoParam
import xyz.eraise.simpleaccountbook.utils.kotlin.async

/**
 * Created by eraise on 2018/2/10.
 */
class DefaultPaymentLiveData: MutableLiveData<Payment>() {

    companion object : SingletonHolderNoParam<DefaultPaymentLiveData>(::DefaultPaymentLiveData)

    init {
        PaymentRepository
                .getDefaultPayment()
                .async()
                .subscribe { value = it }
    }

}