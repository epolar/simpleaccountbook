package xyz.eraise.simpleaccountbook.ui.category.add

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v4.app.Fragment
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
        ViewModelProviders
                .of(getFragment())[AddCategoryViewModel::class.java]
                .addCategory
                .observe(this, Observer { onAdd(it!!) })
    }

    private fun onAdd(newCategory: Category) {
        setResult(
                RESULT_OK,
                Intent().putExtra(Constants.EXTRA_DATA, newCategory))
        finish()
    }

}