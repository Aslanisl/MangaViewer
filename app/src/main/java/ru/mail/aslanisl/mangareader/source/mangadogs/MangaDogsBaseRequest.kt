package ru.mail.aslanisl.mangareader.source.mangadogs

class MangaDogsBaseRequest {

    private val appId = MangaDogsUtils.appId
    private val city: String? = null
    private val country: String? = null
    private val is_vip: Int = 1
    private val lang: String = MangaDogsUtils.systemLanguage
    private val lc_eighteen: String = "1"
    private val lc_hour: String? = null
    private val lc_lat: String? = null
    private val lc_long: String? = null
    private val lc_phone_lang: String? = null
    private val lc_v: String = "8.1.5"
    private val lc_vpn: String = "no"
    private val package_name: String = "com.dogs.nine"
    private val secret = MangaDogsUtils.secret
    private val sign_code: String = MangaDogsUtils.signCode
    private val time_zone: String = MangaDogsUtils.getCurrentTimeZone()

    init {

    }

}