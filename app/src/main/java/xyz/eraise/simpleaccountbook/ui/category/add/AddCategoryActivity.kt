package xyz.eraise.simpleaccountbook.ui.category.add

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import xyz.eraise.simpleaccountbook.pojo.Category
import xyz.eraise.simpleaccountbook.ui.BaseActivity
import xyz.eraise.simpleaccountbook.utils.Constants

/**
 * Created by eraise on 2018/2/10.
 */
class AddCategoryActivity : BaseActivity() {

    override fun createFragment(): Fragment = AddCategoryFragment()

    override fun onStart() {
        super.onStart()
        ViewModelProvider(this)[AddCategoryViewModel::class.java]
            .addCategory
            .observe(this) { onAdd(it!!) }
    }

    private fun onAdd(newCategory: Category) {
        setResult(
            RESULT_OK,
            Intent().putExtra(Constants.EXTRA_DATA, newCategory)
        )
        finish()
    }

}