package xyz.eraise.simpleaccountbook.utils

import android.widget.Toast
import xyz.eraise.simpleaccountbook.MyApp

/**
 * Created by eraise on 2018/2/22.
 */
object PromptUtils {

    fun toast(resId: Int) {
        Toast.makeText(MyApp.instance, resId, Toast.LENGTH_SHORT).show()
    }

    fun toast(text: String) {
        Toast.makeText(MyApp.instance, text, Toast.LENGTH_SHORT).show()
    }

}