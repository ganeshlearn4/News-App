package com.newsapp.app.ui.fragments.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsapp.app.ui.adapters.BookMarkedNewsAdapter
import com.newsapp.app.ui.adapters.NewsAdapter
import com.newsapp.data.model.Article
import com.newsapp.databinding.FragmentBookmarksBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookmarksFragment : Fragment(), BookMarkedNewsAdapter.OnDeleteClickListener {
    private val viewModel by viewModels<BookmarksViewModel>()

    private lateinit var _binding: FragmentBookmarksBinding
    private lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var bookMarkedNewsAdapter: BookMarkedNewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeChanges()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.bookMarkedArticles.value.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "There's no article in bookmarks", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun observeChanges() {
        viewModel.bookMarkedArticles.observe(viewLifecycleOwner) {
            if (_binding.recyclerView.adapter == null) {
                val dividerItemDecoration =
                    DividerItemDecoration(requireContext(), LinearLayout.VERTICAL)
                linearLayoutManager = LinearLayoutManager(requireContext())

                _binding.recyclerView.layoutManager = linearLayoutManager
                _binding.recyclerView.addItemDecoration(dividerItemDecoration)

                bookMarkedNewsAdapter.setContext(requireContext())
                bookMarkedNewsAdapter.setOnDeleteClickListener(this)

                _binding.recyclerView.adapter = bookMarkedNewsAdapter

                bookMarkedNewsAdapter.setArticles(it)
            } else {
                bookMarkedNewsAdapter.setArticles(it)
            }
        }
    }

    override fun onClick(article: Article) {
        viewModel.deleteBookMarkedArticle(article)
        Toast.makeText(requireContext(), "Removed from bookmarks", Toast.LENGTH_LONG).show()
    }
}