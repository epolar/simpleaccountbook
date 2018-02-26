package xyz.eraise.simpleaccountbook.ui.category.add

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import xyz.eraise.simpleaccountbook.pojo.Category

/**
 * Created by eraise on 2018/2/24.
 */
class AddCategoryViewModel : ViewModel() {

    val addCategory = MutableLiveData<Category>()

}