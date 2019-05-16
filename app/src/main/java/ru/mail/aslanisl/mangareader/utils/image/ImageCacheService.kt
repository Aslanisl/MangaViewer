package ru.mail.aslanisl.mangareader.utils.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import com.jakewharton.disklrucache.DiskLruCache
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.ObjectOutputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

private const val APP_VERSION = 1
private const val IO_BUFFER_SIZE = 8 * 1024
private const val MAX_DISK_CACHE_BYTES = 100 * 1024 * 1024L // 100 mb
private const val COMPRESS_QUALITY = 75

private const val INDEX_BITMAP = 0

class ImageCacheService constructor(private val context: Context) {

    fun loadImage(cacheKey: String): Bitmap? {
        val key = md5(cacheKey)
        key ?: return null

        val diskCache = openDiskCache()
        try {
            val snapshot = diskCache?.get(key) ?: return null
            val inputStream = snapshot.getInputStream(INDEX_BITMAP)
            val buffIn = BufferedInputStream(inputStream, IO_BUFFER_SIZE)
            val bitmap = BitmapFactory.decodeStream(buffIn)
            inputStream.close()
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            diskCache?.close()
        }
    }

    fun saveBitmap(cacheKey: String, bitmap: Bitmap?) {
        val key = md5(cacheKey)
        key ?: return

        val diskCache = openDiskCache()
        try {
            val editor = diskCache?.edit(key) ?: return
            val objectOutputStream = editor.newOutputStream(INDEX_BITMAP)
            if (bitmap == null) {
                ObjectOutputStream(objectOutputStream).writeObject(null)
            } else {
                bitmap.compress(CompressFormat.WEBP, COMPRESS_QUALITY, objectOutputStream)
            }
            objectOutputStream.close()
            editor.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            diskCache?.close()
        }
    }

    fun isBitmapExist(cacheKey: String): Boolean {
        val key = md5(cacheKey)
        key ?: return false

        val diskCache = openDiskCache()
        return try {
            diskCache?.get(key) != null
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            diskCache?.close()
        }
    }

    fun clearCache(cacheKey: String) {
        saveBitmap(cacheKey, null)
    }

    fun clearAllCache() {
        openDiskCache()?.delete()
    }

    private fun openDiskCache(): DiskLruCache? {
        val cacheDir = File(context.cacheDir, "apiCache")
        try {
            return DiskLruCache.open(cacheDir, APP_VERSION, 1, MAX_DISK_CACHE_BYTES)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun md5(s: String?): String? {
        val key = s?.trim() ?: return null
        try {
            // Create MD5 Hash
            val digest = MessageDigest.getInstance("MD5")
            digest.update(key.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (message in messageDigest) {
                var h = Integer.toHexString(0xFF and message.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }

            var ss = hexString.toString()
            if (ss.length == 31) ss = "0$s"
            return ss
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return null
    }
}