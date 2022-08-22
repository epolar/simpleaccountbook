package xyz.eraise.simpleaccountbook.ui.category.list

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.launch
import xyz.eraise.simpleaccountbook.databinding.FragmentListCategoryBinding
import xyz.eraise.simpleaccountbook.pojo.Category
import xyz.eraise.simpleaccountbook.repository.CategoryRepository
import xyz.eraise.simpleaccountbook.ui.category.add.AddCategoryActivity
import xyz.eraise.simpleaccountbook.utils.Constants.Companion.EXTRA_DATA

/**
 * Created by eraise on 2018/2/24.
 */
class CategoryListFragment : Fragment() {

    companion object {
        private const val REQUEST_ADD = 1
    }

    private val mAdapter = CategoryAdapter()

    private var _binding: FragmentListCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoryBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            startActivityForResult(
                Intent(context, AddCategoryActivity::class.java),
                REQUEST_ADD
            )
        }
        initRecyclerView()

        initData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_ADD ->
                onAddCategory(data?.getParcelableExtra(EXTRA_DATA)!!)
        }
    }

    private fun initRecyclerView() {
        mAdapter.setOnItemChildClickListener { _, _, position ->
            setDefault(position)
        }
        mAdapter.setOnItemClickListener { _, _, position ->
            selectItem(position)
        }

        binding.rvContent.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
        binding.rvContent.adapter = mAdapter
    }

    private fun initData() {
        lifecycleScope.launch {
            CategoryRepository
                .getCategorysAsync()
                .await()
                .let {
                    mAdapter.addData(it)
                }
        }
    }

    private fun onAddCategory(category: Category) {
        mAdapter.addData(category)
    }

    /**
     * 保存的时候，会清除掉上一个默认的，所以不需要把原先旧的先清除掉，
     * 但需要设置为false，因为以让界面生效
     */
    private fun setDefault(position: Int) {
        val oldDefaultPosition = mAdapter.mDefaultPosition
        if (oldDefaultPosition == position) {
            return
        }

        if (oldDefaultPosition >= 0) {
            val oldDefault = mAdapter.data[oldDefaultPosition]
            oldDefault.isDefault = false

            mAdapter.notifyItemChanged(oldDefaultPosition)
        }

        val default = mAdapter.data[position]
        default.isDefault = true

        lifecycleScope.launch {
            CategoryRepository
                .saveCategoryAsync(default)
                .await()
        }

        mAdapter.notifyItemChanged(position)
    }

    private fun selectItem(position: Int) {
        ViewModelProvider(requireActivity())[SelectCategoryViewModel::class.java]
            .selected
            .postValue(mAdapter.data[position])
    }

}