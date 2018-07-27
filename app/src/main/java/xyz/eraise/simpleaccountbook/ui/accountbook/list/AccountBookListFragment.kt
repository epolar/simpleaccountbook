package xyz.eraise.simpleaccountbook.ui.accountbook.list

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
import kotlinx.android.synthetic.main.fragment_list_account_book.*
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.pojo.AccountBook
import xyz.eraise.simpleaccountbook.repository.AccountBookRepository
import xyz.eraise.simpleaccountbook.ui.accountbook.add.AddAccountBookActivity
import xyz.eraise.simpleaccountbook.utils.Constants.Companion.EXTRA_DATA
import xyz.eraise.simpleaccountbook.utils.kotlin.async

/**
 * Created by eraise on 2018/2/23.
 */
class AccountBookListFragment : Fragment() {

    companion object {
        private const val REQUEST_ADD = 1
    }

    private val mAdapter = AccountBookAdapter()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?)
            :View? =
            inflater.inflate(R.layout.fragment_list_account_book,
                    container,
                    false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        btn_add.setOnClickListener {
            startActivityForResult(
                    Intent(context, AddAccountBookActivity::class.java),
                    REQUEST_ADD)
        }

        initData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_ADD -> {
                onAdd(data?.getParcelableExtra(EXTRA_DATA)!!)
            }
        }
    }

    private fun initData() {
        AccountBookRepository
                .getAccountBooks()
                .async()
                .subscribe( Consumer { mAdapter.addData(0, it) } )
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
                DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        rv_content.adapter = mAdapter
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

        AccountBookRepository
                .saveAccountBook(default)
                .subscribe()

        mAdapter.notifyItemChanged(position)
    }

    private fun onAdd(new: AccountBook) = mAdapter.addData(new)

    private fun selectItem(position: Int) {
        ViewModelProviders
                .of(this)[SelectAccountBookViewModel::class.java]
                .selected
                .postValue(mAdapter.data[position])
    }

}