package ru.mail.aslanisl.mangareader.features.dialog

import androidx.annotation.StringRes
import ru.mail.aslanisl.mangareader.getString

class AlertBuilder {
    val items = mutableListOf<DialogModel>()
}

fun AlertBuilder.okButton(dismissDialog: Boolean = true, listener: (() -> Unit)?) {
    items.add(DialogModel(getString(android.R.string.ok), listener, dismissDialog))
}

fun AlertBuilder.cancelButton(dismissDialog: Boolean = true, listener: (() -> Unit)?) {
    items.add(DialogModel(getString(android.R.string.cancel), listener, dismissDialog))
}

fun AlertBuilder.dismissListener(listener: (() -> Unit)?) {
    items.add(DialogModel(null, listener))
}

fun AlertBuilder.customButton(title: String, dismissDialog: Boolean = true, listener: (() -> Unit)?) {
    items.add(DialogModel(title, listener, dismissDialog))
}

fun AlertBuilder.customButton(@StringRes titleRes: Int, dismissDialog: Boolean = true, listener: (() -> Unit)?) {
    items.add(DialogModel(getString(titleRes), listener, dismissDialog))
}