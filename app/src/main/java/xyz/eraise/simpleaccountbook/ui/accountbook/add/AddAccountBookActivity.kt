package xyz.eraise.simpleaccountbook.ui.accountbook.add

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
        ViewModelProvider(this)[AddAccountBookViewModel::class.java]
            .addAccountBook
            .observe(this) { onAdd(it!!) }
    }

    private fun onAdd(newAccountBook: AccountBook) {
        setResult(RESULT_OK, Intent().putExtra(EXTRA_DATA, newAccountBook))
        finish()
    }

}