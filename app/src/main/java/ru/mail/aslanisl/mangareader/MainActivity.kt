package ru.mail.aslanisl.mangareader

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView.LayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val mangaAdapter by lazy { MangaAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search.setOnClickListener { search() }

        mangasList.layoutManager = LinearLayoutManager(this)
        mangasList.adapter = mangaAdapter

        mangaAdapter.listener = { mangaInfo -> MangaDetailsActivity.openManga(this, mangaInfo) }
    }

    private fun search() {
        val callback = object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                response.body()?.let { parseString(it) }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }
        }
        ApiService.api.search(searchEdit.text.toString()).enqueue(callback)
    }

    private fun parseString(string: String) {
        val mangas = mutableListOf<MangaInfo>()
        val jsonArray = JSONArray(string)
        for (i in 0 until jsonArray.length()) {
            val element = jsonArray[i] as JSONArray
            val manga = MangaInfo(
                ApiService.convertPhotoUrl(element[0] as String),
                element[1] as String,
                element[3] as String
            )
            mangas.add(manga)
        }

        updateMangas(mangas)
    }

    private fun updateMangas(mangas: List<MangaInfo>) {
        mangaAdapter.updateMangas(mangas)
    }
}
