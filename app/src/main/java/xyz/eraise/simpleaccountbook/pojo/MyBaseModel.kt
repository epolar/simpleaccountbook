package xyz.eraise.simpleaccountbook.pojo

import android.os.Parcel
import com.dbflow5.annotation.Column
import com.dbflow5.annotation.PrimaryKey
import com.dbflow5.database.DatabaseWrapper
import com.dbflow5.structure.BaseModel
import io.reactivex.rxjava3.core.Single

/**
 * Created by eraise on 2018/1/22.
 */
open class MyBaseModel : BaseModel() {

    @PrimaryKey(autoincrement = true)
    @Column(name = "id")
    var id: Long = 0

    /**
     * 删除状态
     */
    @Column(name = "is_delete")
    var isDelete: Boolean = false

    /**
     * 最后一次修改时间
     */
    @Column(name = "last_modify_time")
    var lastModifyTime: Long = 0

    override fun save(wrapper: DatabaseWrapper): Boolean {
        lastModifyTime = System.currentTimeMillis()
        return super.save(wrapper)
    }

    fun readFromParcel(parcel: Parcel) {
        id = parcel.readLong()
        isDelete = parcel.readByte() != 0.toByte()
        lastModifyTime = parcel.readLong()
    }

    fun writeToParcel(p0: Parcel?) {
        p0?.writeLong(id)
        p0?.writeByte(if (isDelete) 1 else 0)
        p0?.writeLong(lastModifyTime)
    }

}