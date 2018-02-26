package xyz.eraise.simpleaccountbook.ui.accountbook.add

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_add_account_book.*
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.pojo.AccountBook
import xyz.eraise.simpleaccountbook.repository.AccountBookRepository
import xyz.eraise.simpleaccountbook.utils.PromptUtils

/**
 * Created by eraise on 2018/2/22.
 */
class AddAccountBookFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View?
            = inflater.inflate(
            R.layout.fragment_add_account_book, container,
            false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_save.setOnClickListener { save() }
    }

    private fun save() {
        val name = et_account_book_name.text.toString()
        if (name.isEmpty()) {
            PromptUtils.toast(R.string.add_account_book_name_should_not_be_empty)
            return
        }
        val newAccountBook = AccountBook(name)
        AccountBookRepository
                .saveAccountBook(newAccountBook)
                .subscribe( Consumer {
                    if (it) onSaveSuccess(newAccountBook)
                })
    }

    private fun onSaveSuccess(newAccountBook : AccountBook) {
        ViewModelProviders
                .of(this)[AddAccountBookViewModel::class.java]
                .addAccountBook
                .postValue(newAccountBook)
    }

}