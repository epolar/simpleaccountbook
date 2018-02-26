package xyz.eraise.simpleaccountbook.livedata

import android.arch.lifecycle.MutableLiveData
import xyz.eraise.simpleaccountbook.pojo.Category
import xyz.eraise.simpleaccountbook.repository.CategoryRepository
import xyz.eraise.simpleaccountbook.utils.SingletonHolderNoParam
import xyz.eraise.simpleaccountbook.utils.kotlin.async

/**
 * Created by eraise on 2018/2/10.
 */
class DefaultCategoryLiveData : MutableLiveData<Category>() {

    companion object : SingletonHolderNoParam<DefaultCategoryLiveData>(::DefaultCategoryLiveData)

    init {
        CategoryRepository
                .getDefaultCategory()
                .async()
                .subscribe { value = it }
    }

}