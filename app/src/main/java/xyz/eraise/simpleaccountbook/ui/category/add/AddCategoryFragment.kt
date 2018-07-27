package xyz.eraise.simpleaccountbook.ui.category.add

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_add_category.*
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.pojo.Category
import xyz.eraise.simpleaccountbook.repository.CategoryRepository
import xyz.eraise.simpleaccountbook.ui.accountbook.add.AddAccountBookViewModel
import xyz.eraise.simpleaccountbook.utils.PromptUtils

/**
 * Created by eraise on 2018/2/10.
 */
class AddCategoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View?
            = inflater.inflate(
            R.layout.fragment_add_category, container,
            false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_save.setOnClickListener { save() }
    }

    private fun save() {
        val name = et_category_name.text.toString()
        if (name.isEmpty()) {
            PromptUtils.toast(R.string.add_payment_name_should_not_be_empty)
            return
        }
        val category = Category(name)
        CategoryRepository
                .saveCategory(category)
                .subscribe( Consumer {
                    if (it)
                        onSaveSuccess(category)
                } )
    }

    private fun onSaveSuccess(newCategory : Category) {
        ViewModelProviders
                .of(this)[AddCategoryViewModel::class.java]
                .addCategory
                .postValue(newCategory)
    }

}