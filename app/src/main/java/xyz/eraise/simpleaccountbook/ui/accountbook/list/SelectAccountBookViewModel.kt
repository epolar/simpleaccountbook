package xyz.eraise.simpleaccountbook.ui.accountbook.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.eraise.simpleaccountbook.pojo.AccountBook

/**
 * Created by eraise on 2018/2/24.
 */
class SelectAccountBookViewModel : ViewModel() {

    val selected = MutableLiveData<AccountBook>()

}