package xyz.eraise.simpleaccountbook.ui.accountbook.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.databinding.FragmentAddAccountBookBinding
import xyz.eraise.simpleaccountbook.pojo.AccountBook
import xyz.eraise.simpleaccountbook.repository.AccountBookRepository
import xyz.eraise.simpleaccountbook.utils.PromptUtils

/**
 * Created by eraise on 2018/2/22.
 */
class AddAccountBookFragment : Fragment() {

    private var _binding: FragmentAddAccountBookBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAccountBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener { save() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun save() {
        val name = binding.etAccountBookName.text.toString()
        if (name.isEmpty()) {
            PromptUtils.toast(R.string.add_account_book_name_should_not_be_empty)
            return
        }
        val newAccountBook = AccountBook(name)
        lifecycleScope.launch {
            AccountBookRepository
                .saveAccountBook(newAccountBook)
                .await().let {
                    if (it) onSaveSuccess(newAccountBook)
                }
        }
    }

    private fun onSaveSuccess(newAccountBook: AccountBook) {
        ViewModelProvider(this)[AddAccountBookViewModel::class.java]
            .addAccountBook
            .postValue(newAccountBook)
    }

}