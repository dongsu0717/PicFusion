package com.dongsu.data.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.IOException

const val All_ALBUM_NAME = "전체 사진"
const val ALL_ALBUM_ID = -10

fun stringToBitmap(encodedString: String): Bitmap {
    try {
        val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        return bitmap
    } catch (e: Exception) {
        throw IOException("비트맵으로 변환 실패", e)
    }
}
