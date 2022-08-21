package xyz.eraise.simpleaccountbook.ui.tally.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.launch
import xyz.eraise.simpleaccountbook.databinding.FragmentListTallyBinding
import xyz.eraise.simpleaccountbook.repository.TallyRepository

/**
 * Created by eraise on 2018/2/24.
 */
class TallyListFragment : Fragment() {

    private val mAdapter = TallyAdapter()
    private var _binding: FragmentListTallyBinding? = null
    private val binding get() = _binding!!
    private var page = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListTallyBinding
            .inflate(
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

        initRecyclerView()
        initData()
    }

    private fun initRecyclerView() {
        binding.rvContent.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            loadData(page)
        }

        binding.rvContent.adapter = mAdapter
    }

    private fun initData() {
        loadData()
    }

    private fun loadData(pageIndex: Int = 0, pageSize: Int = 20) {
        lifecycleScope.launch {
            TallyRepository
                .getTallyAsync(pageIndex, pageSize)
                .await().let {
                    if (it.size == 0) {
                        mAdapter.loadMoreModule.isEnableLoadMore = false
                    }
                    if (pageIndex == 0) {
                        mAdapter.data = mutableListOf()
                    }
                    mAdapter.addData(it)
                    page = pageIndex + 1
                    mAdapter.loadMoreModule.loadMoreComplete()
                }
        }
    }

}