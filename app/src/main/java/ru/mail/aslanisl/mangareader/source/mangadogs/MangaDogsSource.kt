package ru.mail.aslanisl.mangareader.source.mangadogs

import android.os.Build
import ru.mail.aslanisl.mangareader.data.base.UIData
import ru.mail.aslanisl.mangareader.data.model.Genre
import ru.mail.aslanisl.mangareader.data.model.Manga
import ru.mail.aslanisl.mangareader.data.model.MangaDetails
import ru.mail.aslanisl.mangareader.data.model.Page
import ru.mail.aslanisl.mangareader.network.ApiBuilder
import ru.mail.aslanisl.mangareader.source.IMangaSource

private const val BASE_URL = "https://api2.niadd.com/"

/**
 * curl -X POST \
https://api2.niadd.com/book/search/ \
-H 'Host: api2.niadd.com' \
-H 'Postman-Token: be31a426-16a0-47ba-a454-8e9c39b8c4bd' \
-H 'accept-encoding: gzip' \
-H 'cache-control: no-cache' \
-H 'content-length: 341' \
-H 'content-type: application/json; charset=UTF-8' \
-H 'cookie: __cfduid=dd72cde7805ea28c0ad99ec48c4caa3591559296992; PHPSESSID=jgla3nr46r9d6k7sg0uu7m7qc3' \
-H 'user-agent: Android/Package:com.dogs.nine - Version Name:8.1.5 - Phone Info:MX5(Android Version:5.1)' \
-b '__cfduid=dd72cde7805ea28c0ad99ec48c4caa3591559296992; PHPSESSID=jgla3nr46r9d6k7sg0uu7m7qc3' \
-d '{"keyword":"test.","page":1,"page_size":20,"appId":"201901251841018","is_vip":0,"lang":"ru","lc_dev":"","lc_eighteen":"0","lc_hour":"1559297006","lc_phone_lang":"ru-RU","lc_v":"8.1.5","lc_vpn":"yes","package_name":"com.dogs.nine","secret":"b8cb1a7b75c2cfd332d81690076246fc","sign_code":"918eafceb37e0455b7c648c1e00e9a26","time_zone":"+0300"}'
 */

/**
 * curl -X POST \
https://api2.niadd.com/comments/first_show/ \
-H 'Host: api2.niadd.com' \
-H 'Postman-Token: 9d29d53d-b011-4ff3-b8be-89c765aeaf57' \
-H 'accept-encoding: gzip' \
-H 'accept-language: ru-RU' \
-H 'cache-control: no-cache' \
-H 'content-length: 381' \
-H 'content-type: application/json; charset=UTF-8' \
-H 'cookie: __cfduid=db1d84b7258e238f9bf3b6c82e49b66c21559297001; PHPSESSID=bsco83n6s0ottgkc7pep3sn841' \
-H 'user-agent: android/Manga Dogs8.1.5 (MX5-5.1)' \
-b '__cfduid=db1d84b7258e238f9bf3b6c82e49b66c21559297001; PHPSESSID=bsco83n6s0ottgkc7pep3sn841' \
-d '{"book_id":"34405","order":"time","page":1,"page_size":20,"sub_num":4,"user_num":4,"appId":"201901251841018","is_vip":0,"lang":"ru","lc_dev":"","lc_eighteen":"0","lc_hour":"1559297006","lc_phone_lang":"ru-RU","lc_v":"8.1.5","lc_vpn":"yes","package_name":"com.dogs.nine","secret":"b8cb1a7b75c2cfd332d81690076246fc","sign_code":"918eafceb37e0455b7c648c1e00e9a26","time_zone":"+0300"}'
 */

class MangaDogsSource : IMangaSource {

    private val api by lazy { ApiBuilder().createRetrofit(BASE_URL).create(MangaDogsApi::class.java) }

    override suspend fun searchManga(term: String): UIData<List<Manga>> {
        Build.VERSION.RELEASE
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loadGenres(): UIData<List<Genre>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loadMangaGenre(genreId: String): UIData<List<Manga>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loadMangaGenre(genreId: String, offset: Int): UIData<List<Manga>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun genrePagingCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loadMangaDetails(idManga: String): UIData<MangaDetails> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loadPages(idChapter: String): UIData<List<Page>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}