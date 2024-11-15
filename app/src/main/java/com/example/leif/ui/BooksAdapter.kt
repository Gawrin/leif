package com.example.leif.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.leif.R
import com.example.leif.data.model.Book
import com.example.leif.databinding.BookLayoutBinding
import com.example.leif.util.ensureHttpsImageUrl

class BooksAdapter (private val onBookClick: (Book) -> Unit) : ListAdapter<Book, BooksAdapter.BookViewHolder>(BookDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = BookLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding, onBookClick)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BookViewHolder(
        private val binding: BookLayoutBinding,
        private val onBookClick: (Book) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.root.setOnClickListener { onBookClick(book) }
            binding.bookTitleTextView.text = book.volumeInfo.title
            val secureBook = book.ensureHttpsImageUrl()
            binding.bookCoverImageView.load(secureBook.volumeInfo.imageLinks?.thumbnail) {
                crossfade(true)
                placeholder(R.drawable.nada)
                error(R.drawable.nada)
            }
        }
    }

    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }
}