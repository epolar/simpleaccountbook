package xyz.eraise.simpleaccountbook.ui.category.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.databinding.FragmentAddCategoryBinding
import xyz.eraise.simpleaccountbook.pojo.Category
import xyz.eraise.simpleaccountbook.repository.CategoryRepository
import xyz.eraise.simpleaccountbook.utils.PromptUtils

/**
 * Created by eraise on 2018/2/10.
 */
class AddCategoryFragment : Fragment() {

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener { save() }
    }

    private fun save() {
        val name = binding.etCategoryName.text.toString()
        if (name.isEmpty()) {
            PromptUtils.toast(R.string.add_payment_name_should_not_be_empty)
            return
        }
        val category = Category(name)
        lifecycleScope.launch{
            CategoryRepository
                .saveCategory(category)
                .await()
                .let {
                    if (it)
                        onSaveSuccess(category)
                }
        }
    }

    private fun onSaveSuccess(newCategory: Category) {
        ViewModelProvider(this)[AddCategoryViewModel::class.java]
            .addCategory
            .postValue(newCategory)
    }

}