package xyz.eraise.simpleaccountbook.ui.accountbook.list

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
        ViewModelProvider(this)[SelectAccountBookViewModel::class.java]
            .selected
            .observe(this) { selected(it!!) }
    }

    private fun selected(accountBook: AccountBook) {
        val result = Intent()
        result.putExtra(EXTRA_DATA, accountBook)
        setResult(RESULT_OK, result)
        finish()
    }
}