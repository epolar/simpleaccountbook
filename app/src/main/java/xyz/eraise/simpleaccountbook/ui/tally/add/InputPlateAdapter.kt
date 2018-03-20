package xyz.eraise.simpleaccountbook.ui.tally.add

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import xyz.eraise.simpleaccountbook.MyApp
import xyz.eraise.simpleaccountbook.R

/**
 * Created by eraise on 2018/2/26.
 */
class InputPlateAdapter
    : BaseQuickAdapter<CharSequence,
        BaseViewHolder>(R.layout.item_input_plate) {

    override fun convert(helper: BaseViewHolder?, item: CharSequence?) {
        helper?.setText(R.id.btn, item)
    }

    /**
     * 添加图片项
     * @param resId 图片id
     */
    fun addDrawableItem(resId: Int) {
        val drawable = MyApp.instance.resources
                .getDrawable(resId, MyApp.instance.theme)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val ssb = SpannableStringBuilder()
        ssb.append("d")
        ssb.setSpan(ImageSpan(drawable),
                0,
                1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        addData(ssb)
    }

}