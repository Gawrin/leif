package com.example.leif.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val id: String,
    val volumeInfo: VolumeInfo
) : Parcelable

@Parcelize
data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val publishedDate: String?,
    val pageCount: Int?,
    val categories: List<String>?,
    val imageLinks: ImageLinks?,
    val publisher: String?
) : Parcelable

@Parcelize
data class ImageLinks(
    val thumbnail: String?
) : Parcelable

data class BooksResponse(
    val items: List<Book>?
)
