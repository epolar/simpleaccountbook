package xyz.eraise.simpleaccountbook.ui.accountbook.list

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import xyz.eraise.simpleaccountbook.pojo.AccountBook

/**
 * Created by eraise on 2018/2/24.
 */
class SelectAccountBookViewModel : ViewModel() {

    val selected = MutableLiveData<AccountBook>()

}