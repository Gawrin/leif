package com.example.leif.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.leif.R
import com.example.leif.data.model.Book
import com.example.leif.databinding.PopoutViewBinding
import com.example.leif.util.ensureHttpsImageUrl
import com.example.leif.util.fromHtml
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import coil.load

class BooksDetails : BottomSheetDialogFragment() {

    private var _binding: PopoutViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PopoutViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Book>(ARG_BOOK)?.let { book ->
            setupBookDetails(book)
        }
    }

    private fun setupBookDetails(book: Book) {
        val secureBook = book.ensureHttpsImageUrl()
        with(binding) {
            bookDetailTitleTextView.text = book.volumeInfo.title
            bookDetailAuthorsTextView.text = book.volumeInfo.authors?.joinToString(", ")

            // Publisher and date
            val publisherText = buildString {
                book.volumeInfo.publisher?.let { append(it) }
                book.volumeInfo.publishedDate?.let {
                    if (isNotEmpty()) append(" • ")
                    append(it.split("-")[0]) // Show only the year
                }
            }
            bookDetailPublisherTextView.text = publisherText
            bookDetailPublisherTextView.isVisible = publisherText.isNotEmpty()

            // Description with HTML formatting
            book.volumeInfo.description?.let { description ->
                bookDetailDescriptionTextView.text = description.fromHtml()
            }
            bookDetailDescriptionLabel.isVisible = !book.volumeInfo.description.isNullOrEmpty()
            bookDetailDescriptionTextView.isVisible = !book.volumeInfo.description.isNullOrEmpty()

            // Categories as chips with improved styling
            categoriesChipGroup.removeAllViews()
            book.volumeInfo.categories?.forEach { category ->
                val chip = Chip(requireContext()).apply {
                    text = category
                    setChipBackgroundColorResource(R.color.surface_variant)
                    setTextColor(resources.getColor(R.color.on_surface_variant, null))
                    textSize = 14f
                    isClickable = false
                }
                categoriesChipGroup.addView(chip)
            }
            categoriesScrollView.isVisible = !book.volumeInfo.categories.isNullOrEmpty()

            // Book cover
            bookDetailCoverImageView.load(secureBook.volumeInfo.imageLinks?.thumbnail) {
                crossfade(true)
                placeholder(R.drawable.nada)
                error(R.drawable.nada)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_BOOK = "book"

        fun newInstance(book: Book) = BooksDetails().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_BOOK, book)
            }
        }
    }
}
