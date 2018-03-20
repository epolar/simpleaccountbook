package xyz.eraise.simpleaccountbook.ui.payment.list

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.pojo.Payment

/**
 * Created by eraise on 2018/2/24.
 */
class PaymentAdapter
    : BaseQuickAdapter<Payment,
        BaseViewHolder>(R.layout.item_category) {

    var mDefaultPosition = -1

    override fun convert(helper: BaseViewHolder?, item: Payment?) {
        helper?.setText(R.id.tv_name, item?.name)
        helper?.addOnClickListener(R.id.rbtn_default)
        helper?.setChecked(R.id.rbtn_default, item?.isDefault!!)
        if (item?.isDefault!!) {
            mDefaultPosition = helper?.adapterPosition!!
        }
    }
}