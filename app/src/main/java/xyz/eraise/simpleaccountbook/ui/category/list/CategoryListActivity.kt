package xyz.eraise.simpleaccountbook.ui.category.list

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import xyz.eraise.simpleaccountbook.pojo.Category
import xyz.eraise.simpleaccountbook.ui.BaseActivity
import xyz.eraise.simpleaccountbook.utils.Constants.Companion.EXTRA_DATA

/**
 * Created by eraise on 2018/2/24.
 */
class CategoryListActivity : BaseActivity() {

    override fun createFragment(): Fragment = CategoryListFragment()

    override fun onStart() {
        super.onStart()

        ViewModelProvider(this)[SelectCategoryViewModel::class.java]
            .selected
            .observe(this) { onSelected(it!!) }
    }

    private fun onSelected(category: Category) {
        setResult(
            Activity.RESULT_OK,
            Intent().putExtra(EXTRA_DATA, category)
        )
        finish()
    }

}