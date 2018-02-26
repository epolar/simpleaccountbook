package xyz.eraise.simpleaccountbook.ui.accountbook.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v4.app.Fragment
import xyz.eraise.simpleaccountbook.pojo.AccountBook
import xyz.eraise.simpleaccountbook.ui.BaseActivity
import xyz.eraise.simpleaccountbook.utils.Constants.Companion.EXTRA_DATA

/**
 * Created by eraise on 2018/2/23.
 */
class AccountBookListActivity : BaseActivity() {

    private val fragment = AccountBookListFragment()

    override fun createFragment(): Fragment = fragment

    override fun onStart() {
        super.onStart()
        ViewModelProviders
                .of(fragment)[SelectAccountBookViewModel::class.java]
                .selected
                .observe(this, Observer { selected(it!!) } )
    }

    private fun selected(accountBook: AccountBook) {
        val result = Intent()
        result.putExtra(EXTRA_DATA, accountBook)
        setResult(RESULT_OK, result)
        finish()
    }
}