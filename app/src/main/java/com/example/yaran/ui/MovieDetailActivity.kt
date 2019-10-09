package com.example.yaran.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ScreenUtils
import com.example.yaran.R
import com.example.yaran.base.BaseActivity
import com.example.yaran.di.GlideApp
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.activity_movie.tv_description
import kotlinx.android.synthetic.main.activity_movie.tv_title
import kotlinx.android.synthetic.main.item_movie.iv_image

class MovieDetailActivity : BaseActivity(layout = R.layout.activity_movie) {

    private val viewModel by viewModels<MovieViewModel> { MovieViewModel.Factory(this) }


    override fun viewIsReady(savedInstanceState: Bundle?) {
        ScreenUtils.setFullScreen(this)

        val imdbId = intent.getStringExtra(KEY_IMDB_ID) ?: return
        val title = intent.getStringExtra(KEY_TITLE)
        val posterUrl = intent.getStringExtra(KEY_POSTER_URL)

        GlideApp.with(this).load(posterUrl).into(iv_image)
        GlideApp.with(this).load(posterUrl).into(iv_background)

        tv_title.text = title

        viewModel.getMovie(imdbId)

        viewModel.movieLiveData.observe(this, Observer { movie ->

            tv_description.text = movie.plot
            tv_actors.text = movie.actors
            rb.rating = movie.imdbRating.div(2)
            tv_rate.text = movie.imdbRating.toString()

            tv_detail.text=getString(R.string.movie_detail,movie.genre,movie.year,movie.runtime)

            tv_votes.text=getString(R.string.votes,movie.imdbVotes)

        })


        iv_back.setOnClickListener { finish() }


    }

    companion object {
        const val KEY_IMDB_ID = "IMDB_ID"
        const val KEY_TITLE = "TITLE"
        const val KEY_POSTER_URL = "POSTER_URL"


    }

}