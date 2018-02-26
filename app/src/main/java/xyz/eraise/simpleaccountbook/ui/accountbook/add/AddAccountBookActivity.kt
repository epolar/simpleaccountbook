package xyz.eraise.simpleaccountbook.ui.accountbook.add

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v4.app.Fragment
import xyz.eraise.simpleaccountbook.pojo.AccountBook
import xyz.eraise.simpleaccountbook.ui.BaseActivity
import xyz.eraise.simpleaccountbook.utils.Constants.Companion.EXTRA_DATA

/**
 * Created by eraise on 2018/2/10.
 */
class AddAccountBookActivity : BaseActivity() {

    private val fragment = AddAccountBookFragment()

    override fun createFragment(): Fragment = fragment

    override fun onStart() {
        super.onStart()
        ViewModelProviders
                .of(fragment)[AddAccountBookViewModel::class.java]
                .addAccountBook
                .observe(this, Observer { onAdd(it!!) } )
    }

    private fun onAdd(newAccountBook: AccountBook) {
        setResult(RESULT_OK, Intent().putExtra(EXTRA_DATA, newAccountBook))
        finish()
    }

}