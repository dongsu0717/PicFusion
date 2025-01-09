package com.dongsu.presentation.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.media.ThumbnailUtils
import android.util.Base64
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream

fun bitmapToThumbnail(bitmap: Bitmap, width: Int, height: Int): Bitmap {
    return ThumbnailUtils.extractThumbnail(bitmap, width, height)
}

fun bitmapToString(bitmap: Bitmap): Result<String> =
    runCatching {
        Timber.e("Data Module 로 보내기위한 비트맵을 byteArray로 변환 시작")
        val temp: String
        ByteArrayOutputStream().use { byteArrayOutputStream ->
            Timber.e("ByteArrayOutputStream compress 시작")
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            Timber.e("ByteArrayOutputStream compress 끝")
            val bytes = byteArrayOutputStream.toByteArray()
            temp = Base64.encodeToString(bytes, Base64.DEFAULT)
            Timber.e("Data Module 로 보내기위한 비트맵을 byteArray로 변환 끝")
        }
        temp
    }


fun stringToBitmap(encodedString: String): Bitmap? =
    try {
        if (encodedString.startsWith("/storage")) {
            val bitmap = BitmapFactory.decodeFile(encodedString)
            bitmap
        } else {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            bitmap
        }
    } catch (e: Exception) {
        Timber.e(e)
        null
    }

//svg -> bitmap 동기
suspend fun convertSvgToBitmap(context: Context, svgUri: String) : Bitmap? {

    val loader = ImageLoader.Builder(context)
        .components { add(SvgDecoder.Factory()) }
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
    Timber.e("로더작업")

    val request = ImageRequest.Builder(context)
        .data(svgUri.toByteArray())
        .size(Size.ORIGINAL)
        .build()
    Timber.e("요청")

    val result = loader.execute(request)
    return if (result is SuccessResult) {
        Timber.e("성공!!!!!!!!")
        result.drawable.toBitmap()
    } else {
        Timber.e("실패!!!!!!!!")
        null
    }
}

////svg -> bitmap 비동기
fun convertSvgToBitmap(context: Context, svgUri: String,onBitmapLoaded: (Bitmap) -> Unit) {
    val loader = ImageLoader.Builder(context)
        .components { add(SvgDecoder.Factory()) }
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
    Timber.e("로더작업")

    val request = ImageRequest.Builder(context)
        .data(svgUri.toByteArray())
        .target(
            onSuccess = { result ->
                Timber.e("성공")
                val bitmap = (result as BitmapDrawable).bitmap
                onBitmapLoaded(bitmap)
            },
            onError = {
                Timber.e("실패")

            }
        )
        .build()
    Timber.e("요청")
    loader.enqueue(request)
}


fun combineImages(baseBitmap: Bitmap, overlayBitmap: Bitmap): Bitmap {
    val baseWidth = baseBitmap.width
    val baseHeight = baseBitmap.height

    val overlayAspectRatio = overlayBitmap.width.toFloat() / overlayBitmap.height.toFloat()

    val overlayWidth = baseWidth / 4
    val overlayHeight = (overlayWidth / overlayAspectRatio).toInt()

    val scaledOverlay = Bitmap.createScaledBitmap(
        overlayBitmap,
        overlayWidth,
        overlayHeight,
        false
    )

    val combinedBitmap = Bitmap.createBitmap(baseWidth, baseHeight, baseBitmap.config)
    val canvas = Canvas(combinedBitmap)

    canvas.drawBitmap(baseBitmap, 0f, 0f, null)

    val left = (baseWidth - scaledOverlay.width) / 2f
    val top = (baseHeight - scaledOverlay.height) / 2f
    canvas.drawBitmap(scaledOverlay, left, top, Paint())

    return combinedBitmap
}
