package xyz.eraise.simpleaccountbook.ui.category.list

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import xyz.eraise.simpleaccountbook.pojo.Category

/**
 * Created by eraise on 2018/2/24.
 */
class SelectCategoryViewModel : ViewModel() {

    val selected = MutableLiveData<Category>()

}