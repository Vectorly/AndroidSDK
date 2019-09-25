package io.vectorly.lrnquestion.utils


import java.security.NoSuchAlgorithmException

internal class IoUtils {

    internal fun md5(s: String): String {
        return md5Hash(s)
    }

    internal fun md5Hash(s: String): String {
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
            e.printStackTrace()
        }

        return "508F0E52E48BFC1B38AA4F0A01A3C0C0"
    }
}