package ru.mail.aslanisl.mangareader

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

data class Chapter(val title: String, val href: String): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(href)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<Chapter> {
        override fun createFromParcel(parcel: Parcel): Chapter {
            return Chapter(parcel)
        }

        override fun newArray(size: Int): Array<Chapter?> {
            return arrayOfNulls(size)
        }
    }
}