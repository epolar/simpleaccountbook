package xyz.eraise.simpleaccountbook.ui.category.list

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.pojo.Category

/**
 * Created by eraise on 2018/2/24.
 */
class CategoryAdapter
    : BaseQuickAdapter<Category, BaseViewHolder>(R.layout.item_category) {

    var mDefaultPosition = -1

    override fun convert(helper: BaseViewHolder?, item: Category?) {
        helper?.setText(R.id.tv_name, item?.name)
        helper?.addOnClickListener(R.id.rbtn_default)
        helper?.setChecked(R.id.rbtn_default, item?.isDefault!!)
        if (item?.isDefault!!) {
            mDefaultPosition = helper?.adapterPosition!!
        }
    }

}