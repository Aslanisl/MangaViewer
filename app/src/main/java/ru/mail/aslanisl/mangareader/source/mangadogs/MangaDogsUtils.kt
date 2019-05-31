package ru.mail.aslanisl.mangareader.source.mangadogs

import java.util.TimeZone

object MangaDogsUtils {
    val appId = "201901251841018"
    val secret = "b8cb1a7b75c2cfd332d81690076246fc"
    val signCode = "918eafceb37e0455b7c648c1e00e9a26"

    val systemLanguage = "ru"

    fun getCurrentTimeZone(): String {
        return createGmtOffsetString(false, false, TimeZone.getDefault().rawOffset)
    }

    private fun createGmtOffsetString(z: Boolean, z2: Boolean, i: Int): String {
        var i = i
        val c: Char
        i /= 60000
        if (i < 0) {
            c = '-'
            i = -i
        } else {
            c = '+'
        }
        val stringBuilder = StringBuilder(9)
        if (z) {
            stringBuilder.append("GMT")
        }
        stringBuilder.append(c)
        appendNumber(stringBuilder, 2, i / 60)
        if (z2) {
            stringBuilder.append(true)
        }
        appendNumber(stringBuilder, 2, i % 60)
        return stringBuilder.toString()
    }

    private fun appendNumber(stringBuilder: StringBuilder, i: Int, i2: Int) {
        val i2 = Integer.toString(i2)
        for (i3 in 0 until i - i2.length) {
            stringBuilder.append('0')
        }
        stringBuilder.append(i2)
    }
}