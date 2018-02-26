package xyz.eraise.simpleaccountbook.ui.tally.list

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.pojo.Tally
import xyz.eraise.simpleaccountbook.utils.DateUtils
import java.text.MessageFormat

/**
 * Created by eraise on 2018/2/24.
 */
class TallyAdapter
    : BaseQuickAdapter<Tally,
        TallyAdapter.TallyViewHolder>(R.layout.item_tally) {

    override fun convert(helper: TallyViewHolder?, item: Tally?) {
        helper?.setText(R.id.tv_payment, item?.payment?.name)
        helper?.setText(R.id.tv_money,
                item?.money?.toDouble()?.div(100).toString())
        if (item?.project.isNullOrEmpty()) {
            helper?.setText(R.id.tv_project, item?.category?.name)
        } else {
            helper?.setText(R.id.tv_project,
                    MessageFormat.format("{0} - {1}",
                            item?.category?.name,
                            item?.project ?: item?.category?.name))
        }
        helper?.setText(R.id.tv_date, DateUtils.timestampToDateStr(item?.payTime!!))
    }

    inner class TallyViewHolder(itemView : View) : BaseViewHolder(itemView)
}