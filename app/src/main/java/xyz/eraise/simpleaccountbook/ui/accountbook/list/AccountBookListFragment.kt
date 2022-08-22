package xyz.eraise.simpleaccountbook.ui.accountbook.list

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
import xyz.eraise.simpleaccountbook.databinding.FragmentListAccountBookBinding
import xyz.eraise.simpleaccountbook.pojo.AccountBook
import xyz.eraise.simpleaccountbook.repository.AccountBookRepository
import xyz.eraise.simpleaccountbook.ui.accountbook.add.AddAccountBookActivity
import xyz.eraise.simpleaccountbook.utils.Constants.Companion.EXTRA_DATA

/**
 * Created by eraise on 2018/2/23.
 */
class AccountBookListFragment : Fragment() {

    companion object {
        private const val REQUEST_ADD = 1
    }

    private val mAdapter = AccountBookAdapter()

    private var _binding: FragmentListAccountBookBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListAccountBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        binding.btnAdd.setOnClickListener {
            startActivityForResult(
                Intent(context, AddAccountBookActivity::class.java),
                REQUEST_ADD
            )
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
        lifecycleScope.launch {
            AccountBookRepository
                .getAccountBooksAsync()
                .await()
                .let { mAdapter.addData(it) }
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
            DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        )
        binding.rvContent.adapter = mAdapter
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
            AccountBookRepository
                .saveAccountBookAsync(default)
                .await()
        }

        mAdapter.notifyItemChanged(position)
    }

    private fun onAdd(new: AccountBook) = mAdapter.addData(new)

    private fun selectItem(position: Int) {
        ViewModelProvider(requireActivity())[SelectAccountBookViewModel::class.java]
            .selected
            .postValue(mAdapter.data[position])
    }

}