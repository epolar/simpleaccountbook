package xyz.eraise.simpleaccountbook.pojo

import android.os.Parcel
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper
import io.reactivex.Single

/**
 * Created by eraise on 2018/1/22.
 */
open class MyBaseModel: BaseRXModel() {

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

    override fun save(): Single<Boolean> {
        lastModifyTime = System.currentTimeMillis()
        return super.save()
    }

    override fun save(databaseWrapper: DatabaseWrapper?): Single<Boolean> {
        lastModifyTime = System.currentTimeMillis()
        return super.save(databaseWrapper)
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