package com.example.leif

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.leif.data.model.Book
import com.example.leif.data.repository.BooksRepository
import com.example.leif.databinding.ActivityMainBinding
import com.example.leif.ui.BooksAdapter
import com.example.leif.ui.BooksDetails
import com.example.leif.ui.BooksViewModel
import com.example.leif.ui.BooksViewModelFactory
import com.google.android.material.snackbar.Snackbar
import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: BooksViewModel by viewModels {
        BooksViewModelFactory(BooksRepository())
    }
    private val bookAdapter = BooksAdapter { book ->
        showBookDetails(book)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.booksRecyclerView.apply {

            val spanCount = when {
                resources.configuration.screenWidthDp >= 840 -> 4
                resources.configuration.screenWidthDp >= 600 -> 3
                resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE -> 3
                else -> 2
            }

            layoutManager = GridLayoutManager(this@MainActivity, spanCount)
            adapter = bookAdapter

            if (itemDecorationCount == 0) {
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
                        val position = parent.getChildAdapterPosition(view)
                        val column = position % spanCount

                        outRect.left = spacing - column * spacing / spanCount
                        outRect.right = (column + 1) * spacing / spanCount
                        if (position < spanCount) outRect.top = spacing
                        outRect.bottom = spacing
                    }
                })
            }
        }
    }


    private fun observeViewModel() {
        viewModel.books.observe(this) { books ->
            bookAdapter.submitList(books)
            if (books.isEmpty() && !viewModel.isLoading.value!!) {
                showMessage(getString(R.string.no_results))
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.networkStatusCard?.apply {
                isVisible = isLoading
                if (isLoading) {
                    animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(300)
                        .start()
                } else {
                    animate()
                        .alpha(0f)
                        .translationY(-50f)
                        .setDuration(200)
                        .start()
                }
            }


        }

    }

    private fun showBookDetails(book: Book) {
        BooksDetails.newInstance(book)
            .show(supportFragmentManager, "BookDetailsBottomSheet")
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupRecyclerView()
    }
}