package xyz.eraise.simpleaccountbook.ui.category.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.eraise.simpleaccountbook.pojo.Category

/**
 * Created by eraise on 2018/2/24.
 */
class AddCategoryViewModel : ViewModel() {

    val addCategory = MutableLiveData<Category>()

}