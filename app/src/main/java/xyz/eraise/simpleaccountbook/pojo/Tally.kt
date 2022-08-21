package xyz.eraise.simpleaccountbook.pojo

import com.dbflow5.annotation.Column
import com.dbflow5.annotation.ForeignKey
import com.dbflow5.annotation.ForeignKeyReference
import com.dbflow5.annotation.Table
import xyz.eraise.simpleaccountbook.repository.databases.AppDatabase
import xyz.eraise.simpleaccountbook.utils.DateUtils
import java.util.*

/**
 * Created by eraise on 2018/1/22.
 * 记账条目
 * @property money 金额单位为分
 * @property project 比较详细的项目，如：早餐
 */
@Table(name = "tally", database = AppDatabase::class)
class Tally(
    @ForeignKey(tableClass = AccountBook::class, saveForeignKeyModel = true)
    @ForeignKeyReference(foreignKeyColumnName = "id", columnName = "account_book")
    var accountBook: AccountBook? = null,

    @ForeignKey(tableClass = Payment::class, saveForeignKeyModel = true)
    @ForeignKeyReference(foreignKeyColumnName = "id", columnName = "payment")
    var payment: Payment? = null,

    @ForeignKey(tableClass = Category::class, saveForeignKeyModel = true)
    @ForeignKeyReference(foreignKeyColumnName = "id", columnName = "category")
    var category: Category? = null,

    @Column(name = "project")
    var project: String? = null,

    @Column(name = "money")
    var money: Int = 0,

    payTime: Long = System.currentTimeMillis()
) : MyBaseModel() {
    @Column(name = "pay_time_str")
    var payTimeStr = DateUtils.timestampToStr(payTime)

    @Column(name = "pay_time")
    var payTime: Long = payTime
        set(value) {
            field = value
            payTimeStr = DateUtils.timestampToStr(payTime)
        }

    @Column(name = "year")
    var year: Int = payTimeStr.substring(0, 4).toInt()

    @Column(name = "month")
    var month: Int = payTimeStr.substring(5, 7).toInt()

    @Column(name = "day")
    var day: Int = payTimeStr.substring(8, 10).toInt()
}
