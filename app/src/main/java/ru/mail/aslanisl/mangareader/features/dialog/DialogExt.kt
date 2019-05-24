package ru.mail.aslanisl.mangareader.features.dialog

import android.R.string
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.gone
import ru.mail.aslanisl.mangareader.show

fun Context.showDialog(
    @StringRes messageRes: Int,
    customView: View? = null,
    init: (AlertBuilder.() -> Unit)? = null
) = showDialog(getString(messageRes), customView, init)

fun Context.createDialog(
    @StringRes messageRes: Int,
    customView: View? = null,
    init: (AlertBuilder.() -> Unit)? = null
) = createDialog(getString(messageRes), customView, init)

fun Context.showDialog(
    message: String,
    customView: View? = null,
    init: (AlertBuilder.() -> Unit)? = null
) = createDialog(message, customView, init).show()

fun Context.createDialog(message: String, customView: View?, init: (AlertBuilder.() -> Unit)? = null): AlertDialog {
    val dialogBuilder = AlertDialog.Builder(this)
    val view = LayoutInflater.from(this).inflate(R.layout.custom_dialog_container, null) as ViewGroup

    dialogBuilder.setView(view)
    customView?.let { view.findViewById<ViewGroup>(R.id.custom_dialog_container).addView(it) }

    val messageView = view.findViewById<TextView>(R.id.custom_dialog_message)
    messageView.text = message

    val alertBuilder = AlertBuilder()
    init?.invoke(alertBuilder)
    val items = alertBuilder.items

    if (items.isEmpty()) {
        view.findViewById<View>(R.id.custom_dialog_button_container).gone()
        dialogBuilder.setOnDismissListener {
            customView?.let { view.findViewById<ViewGroup>(R.id.custom_dialog_container).removeAllViews() }
        }
    }
    dialogBuilder.setOnDismissListener {
        customView?.let { view.findViewById<ViewGroup>(R.id.custom_dialog_container).removeAllViews() }
    }
    var dialogLateInit: AlertDialog? = null
    var buttonClicked = false
    for (item in items) {
        if (item.title.isNullOrEmpty()) {
            dialogBuilder.setOnDismissListener {
                if (buttonClicked.not()) item.clickListener?.invoke()
                customView?.let { view.findViewById<ViewGroup>(R.id.custom_dialog_container).removeAllViews() }
            }
            continue
        }

        val buttonView = when (item.title) {
            getString(string.ok) -> view.findViewById<View>(R.id.custom_dialog_ok_button)
            getString(string.cancel) -> view.findViewById<View>(R.id.custom_dialog_cancel_button)
            else -> view.findViewById<View>(R.id.custom_dialog_neutral_button)
        }

        buttonView.show()
        buttonView.setOnClickListener {
            buttonClicked = true
            item.clickListener?.invoke()
            dialogLateInit?.dismiss()
            customView?.let { view.findViewById<ViewGroup>(R.id.custom_dialog_container).removeAllViews() }
        }
    }
    val dialog = dialogBuilder.create()
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialogLateInit = dialog

    return dialog
}