package com.example.mvvmnews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mvvmnews.R
import com.example.mvvmnews.databinding.ActivityNewsBinding
import com.example.mvvmnews.db.ArticleDatabase
import com.example.mvvmnews.repository.NewsRepository

class NewsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityNewsBinding
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository =NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory =NewsViewModelProviderFactory(newsRepository)
        viewModel =ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)
        binding.apply {
            val controller =findNavController(R.id.newsNavHostFragment)
            bottomNavigationView.setupWithNavController(controller)
        }
    }
}