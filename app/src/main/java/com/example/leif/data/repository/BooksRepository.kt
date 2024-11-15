package com.example.leif.data.repository

import com.example.leif.data.api.RetrofitInstance
import com.example.leif.data.model.Book
import com.example.leif.util.ensureHttpsImageUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class BooksRepository {
    private val service = RetrofitInstance.googleBooksService

    suspend fun searchBooks(query: String): List<Book> = withContext(Dispatchers.IO) {
        val response = service.getBooks(query, maxResults = 9)
        val bookIds = response.items?.map { it.id } ?: emptyList()

        bookIds.map { id ->
            async {
                try {
                    service.getBookDetails(id).ensureHttpsImageUrl()
                } catch (e: Exception) {
                    null
                }
            }
        }.mapNotNull { it.await() }
    }
}