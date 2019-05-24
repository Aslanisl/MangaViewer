package ru.mail.aslanisl.mangareader.features.dialog

data class DialogModel(
    val title: String?,
    val clickListener: (() -> Unit)?,
    val dismissDialog: Boolean = true
)