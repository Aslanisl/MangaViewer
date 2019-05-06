package ru.mail.aslanisl.mangareader.features.main

import ru.mail.aslanisl.mangareader.features.base.BaseFragment

interface OnBackPressListener {
    /**
     * return true to hold back press
     * return false if you not need to hold it
     */
    fun onBackPressed(): Boolean
}