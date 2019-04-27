package ru.mail.aslanisl.mangareader.features.base

import android.content.Context
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {

    val contextNotNull: Context
        get() {
            return this@BaseFragment.context ?: throw IllegalStateException("Context must not be null")
        }

}