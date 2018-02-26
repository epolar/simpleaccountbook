package xyz.eraise.simpleaccountbook.ui.category.list

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_list_category.*
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.pojo.Category
import xyz.eraise.simpleaccountbook.repository.CategoryRepository
import xyz.eraise.simpleaccountbook.ui.category.add.AddCategoryActivity
import xyz.eraise.simpleaccountbook.utils.Constants.Companion.EXTRA_DATA
import xyz.eraise.simpleaccountbook.utils.kotlin.async

/**
 * Created by eraise on 2018/2/24.
 */
class CategoryListFragment : Fragment() {

    companion object {
        private const val REQUEST_ADD = 1
    }

    private val mAdapter = CategoryAdapter()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View? =
            inflater.inflate(R.layout.fragment_list_category,
                    container,
                    false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add.setOnClickListener {
            startActivityForResult(
                    Intent(context, AddCategoryActivity::class.java),
                    REQUEST_ADD)
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
        mAdapter.onItemChildClickListener =
                BaseQuickAdapter.OnItemChildClickListener { _, _, position ->
                    setDefault(position)
                }
        mAdapter.onItemClickListener =
                BaseQuickAdapter.OnItemClickListener { _, _, position ->
                    selectItem(position)
                }

        rv_content.addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        rv_content.adapter = mAdapter
    }

    private fun initData() {
        CategoryRepository
                .getCategorys()
                .async()
                .subscribe ( Consumer { mAdapter.addData(0, it) } )
    }

    private fun onAddCategory(category : Category) {
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

        CategoryRepository
                .saveCategory(default)
                .subscribe()

        mAdapter.notifyItemChanged(position)
    }

    private fun selectItem(position : Int) {
        ViewModelProviders
                .of(this)[SelectCategoryViewModel::class.java]
                .selected
                .postValue(mAdapter.data[position])
    }

}