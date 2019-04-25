package ru.mail.aslanisl.mangareader

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

data class MangaInfo(val photoUrl: String, val name: String, val description: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(photoUrl)
        parcel.writeString(name)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<MangaInfo> {
        override fun createFromParcel(parcel: Parcel): MangaInfo {
            return MangaInfo(parcel)
        }

        override fun newArray(size: Int): Array<MangaInfo?> {
            return arrayOfNulls(size)
        }
    }
}
