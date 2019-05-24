package ru.mail.aslanisl.mangareader.features.main

import android.os.Bundle
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import kotlinx.android.synthetic.main.activity_main.*
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.features.base.BaseActivity
import ru.mail.aslanisl.mangareader.features.base.BaseFragment
import ru.mail.aslanisl.mangareader.features.genre.GenreListFragment
import ru.mail.aslanisl.mangareader.features.history.HistoryMangaFragment
import ru.mail.aslanisl.mangareader.features.mangaList.MangaListFragment
import ru.mail.aslanisl.mangareader.features.main.MainItem.GENRE
import ru.mail.aslanisl.mangareader.features.main.MainItem.HISTORY
import ru.mail.aslanisl.mangareader.features.main.MainItem.NONE
import ru.mail.aslanisl.mangareader.features.main.MainItem.SEARCH
import ru.mail.aslanisl.mangareader.features.main.MainItem.SETTINGS
import ru.mail.aslanisl.mangareader.features.settings.SettingsFragment
import ru.mail.aslanisl.mangareader.gone
import ru.mail.aslanisl.mangareader.show

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navView.listener = { selectItem(it) }
        navView.sameItemListener = { holdBackPressedFragment() }
        if (savedInstanceState == null) {
            navView.selectItem(SEARCH)
        }
    }

    private fun selectItem(item: MainItem) {
        var fragment: BaseFragment? = null
        var tag: String? = null
        when (item) {
            SEARCH -> {
                fragment = getMainFragment(MangaListFragment.TAG, item)
                tag = MangaListFragment.TAG
            }

            GENRE -> {
                fragment = getMainFragment(GenreListFragment.TAG, item)
                tag = GenreListFragment.TAG
            }

            HISTORY -> {
                fragment = getMainFragment(HistoryMangaFragment.TAG, item)
                tag = HistoryMangaFragment.TAG
            }
            SETTINGS -> {
                fragment = getMainFragment(SettingsFragment.TAG, item)
                tag = SettingsFragment.TAG
            }

            NONE -> {
            }
        }

        if (fragment == null) return
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment, tag)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    private fun getMainFragment(tag: String, item: MainItem): BaseFragment? {
        return supportFragmentManager
            .findFragmentByTag(tag) as BaseFragment?
            ?: createFragment(item)
    }

    private fun createFragment(item: MainItem): BaseFragment? {
        return when (item) {
            SEARCH -> MangaListFragment.newInstance()
            GENRE -> GenreListFragment.newInstance()
            HISTORY -> HistoryMangaFragment.newInstance()
            SETTINGS -> SettingsFragment.newInstance()
            NONE -> null
        }
    }

    private fun holdBackPressedFragment(): Boolean {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        if (currentFragment is OnBackPressListener && currentFragment.onBackPressed()) {
            return true
        }
        return false
    }

    override fun onBackPressed() {
        if (holdBackPressedFragment()) return

        supportFragmentManager.popBackStackImmediate(null, POP_BACK_STACK_INCLUSIVE)
        finishAffinity()
        super.onBackPressed()
    }
}
