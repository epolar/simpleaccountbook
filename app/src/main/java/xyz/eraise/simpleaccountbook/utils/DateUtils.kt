package xyz.eraise.simpleaccountbook.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by eraise on 2018/2/10.
 */
object DateUtils {

    fun timestampToStr(timestamp: Long): String
            = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(timestamp)

    fun timestampToDateStr(timestamp: Long): String
            = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(timestamp)

}