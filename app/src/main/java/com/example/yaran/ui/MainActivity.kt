package com.example.yaran.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yaran.R
import com.example.yaran.adapter.MovieAdapter
import com.example.yaran.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(R.layout.activity_main) {
    private val viewModel by viewModels<MovieViewModel> { MovieViewModel.Factory(this) }

    private val adapter = MovieAdapter(this)


    override fun viewIsReady(savedInstanceState: Bundle?) {
        initView()

        initViewModel()
    }

    private fun initViewModel() {
        viewModel.moviesLiveData.observe(this, Observer { movies ->
            adapter.submit(movies)
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            pb.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.getMovies()
    }

    private fun initView() {
        rv_movie.layoutManager = LinearLayoutManager(this)
        rv_movie.adapter = adapter
        rv_movie.setHasFixedSize(true)
    }


}
