package ru.mail.aslanisl.mangareader.features.main

import android.os.Bundle
import ru.mail.aslanisl.mangareader.R
import ru.mail.aslanisl.mangareader.features.base.BaseActivity
import ru.mail.aslanisl.mangareader.features.list.MangaListFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, MangaListFragment.newInstance(), MangaListFragment.TAG)
                .commitAllowingStateLoss()
        }
    }
}
