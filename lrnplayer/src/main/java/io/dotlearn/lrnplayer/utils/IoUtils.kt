package io.dotlearn.lrnplayer.utils


import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.security.Key
import java.security.NoSuchAlgorithmException
import javax.crypto.spec.SecretKeySpec

class IoUtils {

    @Throws(IOException::class)
    fun toByteArray(`is`: InputStream): ByteArray {
        val buffer = ByteArrayOutputStream()

        var nRead = -1
        val data = ByteArray(16384)

        while ({ nRead = `is`.read(data, 0, data.size); nRead }() != -1) {
            buffer.write(data, 0, nRead)
        }

        buffer.flush()

        return buffer.toByteArray()
    }

    fun closeQuietly(closeable: Closeable?) {
        try {
            closeable?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun getSecretKey(key: String): Key {
        val encodedKey = Base64.decode(md5(key), Base64.DEFAULT)
        return SecretKeySpec(encodedKey, 0, encodedKey.size, "AES")
    }

    private fun md5(s: String): String {
        val MD5 = "MD5"
        try {
            // Create MD5 Hash
            val digest = java.security.MessageDigest.getInstance(MD5)
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2)
                    h = "0" + h
                hexString.append(h)
            }
            return hexString.toString()

        } catch (e: NoSuchAlgorithmException) {
            Logger.e("Error md5ing: $e")
        }

        return "508F0E52E48BFC1B38AA4F0A01A3C0C0"
    }
}