package xyz.eraise.simpleaccountbook.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
/**
 * Created by eraise on 2018/2/23.
 */
abstract class BaseActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, createFragment(), "content")
                .commit()
    }

    fun getFragment(): Fragment {
        return supportFragmentManager.findFragmentByTag("content")
    }

    abstract fun createFragment(): Fragment

}
