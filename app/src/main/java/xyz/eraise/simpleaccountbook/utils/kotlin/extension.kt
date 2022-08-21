package xyz.eraise.simpleaccountbook.utils.kotlin

import java.util.*


/**
 * Calendar当前保存的日期是否为今天
 */
fun Calendar.isToday(): Boolean {
    val now = Calendar.getInstance()
    return  this[Calendar.YEAR] == now[Calendar.YEAR]
            && this[Calendar.MONTH] == now[Calendar.MONTH]
            && this[Calendar.DAY_OF_MONTH] == now[Calendar.DAY_OF_MONTH]
}