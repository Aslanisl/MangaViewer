package ru.mail.aslanisl.mangareader

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_chapter.*
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val KEY_CHAPTER = "KEY_CHAPTER"

class ChapterActivity : AppCompatActivity() {

    private val adapter by lazy { ImageAdapter() }

    companion object {
        fun openChapter(context: Context, chapter: Chapter) {
            val intent = Intent(context, ChapterActivity::class.java)
            intent.putExtra(KEY_CHAPTER, chapter)
            context.startActivity(intent)
        }
    }

    private lateinit var chapter: Chapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        val chapter = intent?.getParcelableExtra<Chapter>(KEY_CHAPTER)
        if (chapter == null) {
            finish()
            return
        }
        this.chapter = chapter

        val callback = object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                response.body()?.let { parseHtml(it) }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }
        }
        chapterImages.layoutManager = LinearLayoutManager(this)
        chapterImages.adapter = adapter

        ApiService.api.loadChapter(chapter.href).enqueue(callback)
    }

    private fun parseHtml(html: String) {
        val document = Jsoup.parse(html)
        val pagesCount = document
            .getElementsByClass("changepage")[0]
            .getElementById("page")
            .children()
            .size
        loadPages(pagesCount)
    }

    private fun loadPages(pagesCount: Int) {
        val url = chapter.href.replace(".html", ".html")
        val callback = object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                response.body()?.let { parsePagesHtml(it, pagesCount) }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }
        }

        ApiService.api.loadChapter(url).enqueue(callback)
    }

    private fun parsePagesHtml(html: String, pagesCount: Int) {
        val document = Jsoup.parse(html)
        val imagesUrls = mutableListOf<String>()
//        for (i in 1..10) {
            val element = document.getElementsByClass("manga_pic manga_pic_1")[0]
            element?.attr("src")?.let { imagesUrls.add(it) }
//        }
        showPages(imagesUrls)
    }

    private fun showPages(pages: List<String>) {
        adapter.updateImages(pages)
    }
}
