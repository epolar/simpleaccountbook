package xyz.eraise.simpleaccountbook.ui.tally.list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_list_tally.*
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.repository.TallyRepository
import xyz.eraise.simpleaccountbook.utils.kotlin.async

/**
 * Created by eraise on 2018/2/24.
 */
class TallyListFragment : Fragment() {

    private val mAdapter = TallyAdapter()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View? = inflater
            .inflate(
                    R.layout.fragment_list_tally,
                    container,
                    false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initData()
    }

    private fun initRecyclerView() {
        rv_content.addItemDecoration(DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL))
        rv_content.adapter = mAdapter
    }

    private fun initData() {
        TallyRepository
                .getTally()
                .async()
                .subscribe ( Consumer { mAdapter.addData(0, it) } )
    }

}