package xyz.eraise.simpleaccountbook.ui.tally.add

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import androidx.core.content.res.ResourcesCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import xyz.eraise.simpleaccountbook.MyApp
import xyz.eraise.simpleaccountbook.R

/**
 * Created by eraise on 2018/2/26.
 */
class InputPlateAdapter
    : BaseQuickAdapter<CharSequence, BaseViewHolder>(R.layout.item_input_plate) {

    /**
     * 添加图片项
     * @param resId 图片id
     */
    fun addDrawableItem(resId: Int) {
        val drawable = ResourcesCompat.getDrawable(MyApp.instance.resources, resId, MyApp.instance.theme)
        drawable?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
            val ssb = SpannableStringBuilder()
            ssb.append("d")
            ssb.setSpan(
                ImageSpan(it),
                0,
                1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            addData(ssb)
        }
    }

    override fun convert(holder: BaseViewHolder, item: CharSequence) {
        holder.setText(R.id.btn, item)
    }

}