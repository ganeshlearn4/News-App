package com.newsapp.app.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.newsapp.BuildConfig
import com.newsapp.app.ui.activities.login.LoginActivity
import com.newsapp.app.ui.adapters.NewsAdapter
import com.newsapp.data.model.Article
import com.newsapp.databinding.FragmentHomeBinding
import com.newsapp.enums.RequestStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), NewsAdapter.OnBookMarkClickListener,
    NewsAdapter.OnSignOutListener {
    private lateinit var googleSignInClient: GoogleSignInClient
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var _binding: FragmentHomeBinding
    private lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val googleSignInOptions: GoogleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestEmail()
            .requestIdToken(BuildConfig.GOOGLE_SIGN_IN_WEB_CLIENT_ID)
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)

        setListeners()
        observeChanges()
    }

    override fun onResume() {
        super.onResume()
        if (_binding.recyclerView.adapter != null) {
            newsAdapter.setArticles(viewModel.allArticles.value!!)
        }
    }

    private fun setListeners() {
        _binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    viewModel.isScrolling.value = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentItems = linearLayoutManager.childCount
                val totalItems = linearLayoutManager.itemCount
                val scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition()

                if (viewModel.isScrolling.value == true && dy >= 0 && (currentItems + scrollOutItems == totalItems)) {
                    if (viewModel.canLoadNewData.value!!) {
                        viewModel.isScrolling.value = false
                        viewModel.getMoreNews()
                    }
                }
            }
        })
    }

    private fun observeChanges() {
        viewModel.requestStatus.observe(viewLifecycleOwner) {
            when (it) {
                RequestStatus.NONE -> {
                    _binding.progressBar.visibility = View.GONE
                }

                RequestStatus.LOADING -> {
                    _binding.progressBar.visibility = View.VISIBLE
                }

                RequestStatus.SUCCESS -> {
                    _binding.progressBar.visibility = View.GONE

                    if (_binding.recyclerView.adapter == null) {
                        val dividerItemDecoration =
                            DividerItemDecoration(requireContext(), LinearLayout.VERTICAL)
                        linearLayoutManager = LinearLayoutManager(requireContext())

                        _binding.recyclerView.layoutManager = linearLayoutManager
                        _binding.recyclerView.addItemDecoration(dividerItemDecoration)

                        newsAdapter.setContext(requireContext())
                        newsAdapter.setOnBookMarkClickListener(this)
                        newsAdapter.setOnSignOutListener(this)
                        newsAdapter.setArticles(viewModel.newArticles.value!!)

                        _binding.recyclerView.adapter = newsAdapter
                    } else {
                        newsAdapter.addArticles(viewModel.newArticles.value!!)
                    }
                }

                RequestStatus.ERROR -> {
                    _binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error loading news", Toast.LENGTH_LONG).show()
                }

                RequestStatus.NETWORK_ERROR -> {
                    _binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Network Error while loading news", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.newArticles.observe(viewLifecycleOwner) {
            if (viewModel.totalArticlesLoaded.value!! == viewModel.totalArticles.value!!) {
                viewModel.canLoadNewData.value = false
            } else {
                viewModel.allArticles.value?.addAll(it)
                viewModel.totalArticlesLoaded.value =
                    viewModel.totalArticlesLoaded.value?.plus(it.size)
            }
        }

        viewModel.canLoadNewData.observe(viewLifecycleOwner) {
            if (!it) {
                Toast.makeText(requireContext(), "No more data", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClick(article: Article) {
        viewModel.bookmarkArticle(article)
        Toast.makeText(requireContext(), "Added to bookmarks", Toast.LENGTH_LONG).show()
    }

    override fun onSignOut() {
        googleSignInClient.revokeAccess().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Sign-out successful", Toast.LENGTH_LONG).show()
                val loginIntent = Intent(context, LoginActivity::class.java)
                context?.startActivity(loginIntent)
                requireActivity().finish()
            } else {
                Toast.makeText(context, "Error while signing out", Toast.LENGTH_LONG).show()
            }
        }
    }
}