package xyz.eraise.simpleaccountbook.pojo

import android.os.Parcel
import android.os.Parcelable
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.Table
import xyz.eraise.simpleaccountbook.repository.databases.AppDatabase

/**
 * Created by eraise on 2018/2/10.
 * 类别
 */
@Table(name = "category", database = AppDatabase::class)
class Category(@Column(name = "category_name") var name: String? = null,
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

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}