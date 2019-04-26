package ru.mail.aslanisl.mangareader

import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ru.mail.aslanisl.mangareader.dataModel.base.UIData

inline fun <reified T : ViewModel> BaseActivity.getViewModel(viewModelFactory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, viewModelFactory)[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.getViewModel(viewModelFactory: ViewModelProvider.Factory, fromActivity: Boolean = false): T {
    return when (fromActivity) {
        false -> ViewModelProviders.of(this, viewModelFactory)[T::class.java]
        else -> ViewModelProviders.of(activity!!, viewModelFactory)[T::class.java]
    }
}

fun <T> getLoadingLiveData(): MediatorLiveData<UIData<T>> {
    val liveData = MediatorLiveData<UIData<T>>()
    liveData.postValue(UIData.loading())
    return liveData
}