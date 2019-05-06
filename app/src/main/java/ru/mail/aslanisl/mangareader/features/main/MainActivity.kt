package ru.mail.aslanisl.mangareader.features.main

import android.os.Bundle
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

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navView.listener = { selectItem(it) }
        if (savedInstanceState == null) {
            navView.selectItem(SEARCH)
        }
    }

    private fun selectItem(item: MainItem) {
        var fragment: BaseFragment? = null
        var tag: String? = null
        when (item) {
            SEARCH -> {
                fragment = MangaListFragment.newInstance()
                tag = MangaListFragment.TAG
            }

            GENRE -> {
                fragment = GenreListFragment.newInstance()
                tag = GenreListFragment.TAG
            }

            HISTORY -> {
                fragment = HistoryMangaFragment.newInstance()
                tag = HistoryMangaFragment.TAG
            }

            NONE -> {
            }
        }

        if (fragment == null) return
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment, tag)
            .commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        if (currentFragment is OnBackPressListener && currentFragment.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }
}
