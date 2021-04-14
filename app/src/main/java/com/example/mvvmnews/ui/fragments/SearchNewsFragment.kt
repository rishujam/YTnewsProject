package com.example.mvvmnews.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmnews.R
import com.example.mvvmnews.adapters.NewsAdapter
import com.example.mvvmnews.ui.NewsActivity
import com.example.mvvmnews.ui.NewsViewModel
import com.example.mvvmnews.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.mvvmnews.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment: Fragment(R.layout.fragment_search_news) {

    lateinit var viewModel:NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private lateinit var loading:ProgressBar
    private lateinit var rvSearch:RecyclerView
    private lateinit var searchText:EditText
    val TAG ="SearchNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =(activity as NewsActivity).viewModel
        loading=view.findViewById(R.id.paginationProgressBar)
        rvSearch=view.findViewById(R.id.rvSearchNews)
        searchText=view.findViewById(R.id.etSearch)
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle =Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                    R.id.action_searchNewsFragment_to_articleFragment,
                    bundle
            )
        }

        var job:Job?=null
        searchText.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG,"An error occured : $message")
                    }
                }
                is Resource.Loading ->
                    showProgressBar()
            }
        })
    }

    private fun hideProgressBar(){
        loading.visibility =View.INVISIBLE
    }

    private fun showProgressBar(){
        loading.visibility =View.VISIBLE
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        rvSearch.apply {
            adapter =newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}