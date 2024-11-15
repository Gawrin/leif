package com.example.leif.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leif.data.model.Book
import com.example.leif.data.repository.BooksRepository
import kotlinx.coroutines.launch


class BooksViewModel (private val repository: BooksRepository) : ViewModel() {
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        searchBooks("Japanese")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val result = repository.searchBooks(query)
                _books.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}