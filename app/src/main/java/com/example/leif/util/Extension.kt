package com.example.leif.util

import com.example.leif.data.model.Book
import android.os.Build
import android.text.Html
import android.text.Spanned

fun Book.ensureHttpsImageUrl(): Book {
    val httpsImageUrl = this.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://")
    return this.copy(
        volumeInfo = this.volumeInfo.copy(
            imageLinks = this.volumeInfo.imageLinks?.copy(
                thumbnail = httpsImageUrl
            )
        )
    )
}

fun String.fromHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }
}
