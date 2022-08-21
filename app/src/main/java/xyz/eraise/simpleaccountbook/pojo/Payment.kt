package xyz.eraise.simpleaccountbook.pojo

import android.os.Parcel
import android.os.Parcelable
import com.dbflow5.annotation.Column
import com.dbflow5.annotation.Table
import xyz.eraise.simpleaccountbook.repository.databases.AppDatabase

/**
 * Created by eraise on 2018/1/22.
 * 支付方式
 */
@Table(name = "payment", database = AppDatabase::class)
class Payment(@Column(name = "payment_name") var name: String? = null,
              @Column(name = "is_default") var isDefault: Boolean = false)
    : MyBaseModel(), Parcelable {

    constructor(parcel: Parcel) : this() {
        readFromParcel(parcel)
        name = parcel.readString()
        isDefault = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        writeToParcel(p0)
        p0?.writeString(name)
        p0?.writeByte(if (isDefault) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Payment> {
        override fun createFromParcel(parcel: Parcel): Payment {
            return Payment(parcel)
        }

        override fun newArray(size: Int): Array<Payment?> {
            return arrayOfNulls(size)
        }
    }
}