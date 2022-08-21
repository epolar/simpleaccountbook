package xyz.eraise.simpleaccountbook.ui.tally.list

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.pojo.Tally
import xyz.eraise.simpleaccountbook.utils.DateUtils
import java.text.MessageFormat

/**
 * Created by eraise on 2018/2/24.
 */
class TallyAdapter
    : BaseQuickAdapter<Tally, BaseViewHolder>(R.layout.item_tally), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: Tally) {
        holder.setText(R.id.tv_payment, item.payment?.name)
        holder.setText(R.id.tv_money,
            item.money.toDouble().div(100).toString())
        if (item.project.isNullOrEmpty()) {
            holder.setText(R.id.tv_project, item.category?.name)
        } else {
            holder.setText(R.id.tv_project,
                MessageFormat.format("{0} - {1}",
                    item.category?.name,
                    item.project ?: item.category?.name))
        }
        holder.setText(R.id.tv_date, DateUtils.timestampToStr(item.payTime))
    }

}