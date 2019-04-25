package ru.mail.aslanisl.mangareader

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_manga_details.*
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val KEY_MANGA = "KEY_MANGA"

class MangaDetailsActivity : AppCompatActivity() {

    private val adapter by lazy { ChapterAdapter() }

    companion object {
        fun openManga(context: Context, mangaInfo: MangaInfo) {
            val intent = Intent(context, MangaDetailsActivity::class.java)
            intent.putExtra(KEY_MANGA, mangaInfo)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_details)

        val mangaInfo = intent?.getParcelableExtra<MangaInfo>(KEY_MANGA)
        if (mangaInfo == null) {
            finish()
            return
        }

        val callback = object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                response.body()?.let { parseHtml(it) }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }
        }

        chapterList.layoutManager = LinearLayoutManager(this)
        chapterList.adapter = adapter
        adapter.listener = { ChapterActivity.openChapter(this, it) }

        ApiService.api.load(mangaInfo.name).enqueue(callback)
    }

    private fun parseHtml(html: String) {
        val document = Jsoup.parse(html)
        val elements = document.getElementsByClass("sub_vol_ul")
        val parsedChars = elements
            .flatMap { it.children() }
            .map {
                val chapterList = it.getElementsByClass("chapter_list_a")[0]
                Chapter(chapterList.attr("title"), chapterList.attr("href"))
            }
        initChapters(parsedChars)
    }

    private fun initChapters(chapters: List<Chapter>) {
        adapter.updateChapter(chapters)
    }
}
