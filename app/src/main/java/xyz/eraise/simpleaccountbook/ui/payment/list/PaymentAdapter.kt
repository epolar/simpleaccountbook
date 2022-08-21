package xyz.eraise.simpleaccountbook.ui.payment.list

import android.widget.RadioButton
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.pojo.Payment

/**
 * Created by eraise on 2018/2/24.
 */
class PaymentAdapter
    : BaseQuickAdapter<Payment, BaseViewHolder>(R.layout.item_category) {

    var mDefaultPosition = -1

    init {
        addChildClickViewIds(R.id.rbtn_default)
    }

    override fun convert(holder: BaseViewHolder, item: Payment) {
        holder.setText(R.id.tv_name, item.name)
        if (item.isDefault) {
            mDefaultPosition = holder.bindingAdapterPosition
        }
    }
}