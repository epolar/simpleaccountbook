package xyz.eraise.simpleaccountbook.pojo

import android.os.Parcel
import android.os.Parcelable
import com.dbflow5.annotation.Column
import com.dbflow5.annotation.Table
import xyz.eraise.simpleaccountbook.repository.databases.AppDatabase

/**
 * Created by eraise on 2018/1/19.
 * 记账的账本
 */
@Table(name = "account_books", database = AppDatabase::class)
data class AccountBook(@Column(name = "book_name") var name: String? = null,
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

    companion object CREATOR : Parcelable.Creator<AccountBook> {
        override fun createFromParcel(parcel: Parcel): AccountBook {
            return AccountBook(parcel)
        }

        override fun newArray(size: Int): Array<AccountBook?> {
            return arrayOfNulls(size)
        }
    }

}