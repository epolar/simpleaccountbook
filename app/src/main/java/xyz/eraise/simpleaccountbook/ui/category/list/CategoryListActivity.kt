package xyz.eraise.simpleaccountbook.ui.category.list

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v4.app.Fragment
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

        ViewModelProviders
                .of(getFragment())[SelectCategoryViewModel::class.java]
                .selected
                .observe(this, Observer { onSelected(it!!) })
    }

    private fun onSelected(category : Category) {
        setResult(
                Activity.RESULT_OK,
                Intent().putExtra(EXTRA_DATA, category))
        finish()
    }

}