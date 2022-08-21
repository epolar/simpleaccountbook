package xyz.eraise.simpleaccountbook.ui.accountbook.list

import android.widget.RadioButton
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.pojo.AccountBook

/**
 * Created by eraise on 2018/2/23.
 */
class AccountBookAdapter
    : BaseQuickAdapter<AccountBook, BaseViewHolder>(R.layout.item_account_book) {

    var mDefaultPosition: Int = -1

    init {
        addChildClickViewIds(R.id.rbtn_default)
    }

    override fun convert(holder: BaseViewHolder, item: AccountBook) {
        holder.setText(R.id.tv_name, item.name)
        if (item.isDefault) {
            mDefaultPosition = holder.bindingAdapterPosition
        }
        holder.getView<RadioButton>(R.id.rbtn_default).isChecked = item.isDefault
    }

}