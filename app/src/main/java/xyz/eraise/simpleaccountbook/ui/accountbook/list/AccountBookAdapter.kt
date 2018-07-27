package xyz.eraise.simpleaccountbook.ui.accountbook.list

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.pojo.AccountBook

/**
 * Created by eraise on 2018/2/23.
 */
class AccountBookAdapter
    : BaseQuickAdapter<AccountBook,
        BaseViewHolder>(R.layout.item_account_book) {

    var mDefaultPosition : Int = -1

    override fun convert(helper: BaseViewHolder?, item: AccountBook?) {
        helper?.setText(R.id.tv_name, item?.name)
        helper?.addOnClickListener(R.id.rbtn_default)
        if (item?.isDefault!!) {
            mDefaultPosition = helper?.adapterPosition!!
        }
        helper?.setChecked(R.id.rbtn_default, item.isDefault)
    }

}