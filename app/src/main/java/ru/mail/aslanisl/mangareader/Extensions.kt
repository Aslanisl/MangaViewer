package ru.mail.aslanisl.mangareader

import androidx.lifecycle.MediatorLiveData
import ru.mail.aslanisl.mangareader.dataModel.base.UIData

fun <T> getLoadingLiveData(): MediatorLiveData<UIData<T>> {
    val liveData = MediatorLiveData<UIData<T>>()
    liveData.postValue(UIData.loading())
    return liveData
}