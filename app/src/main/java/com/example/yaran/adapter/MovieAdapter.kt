package com.example.yaran.adapter

import android.app.ActivityOptions
import android.content.Intent
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.StringUtils
import com.example.yaran.R
import com.example.yaran.di.GlideApp
import com.example.yaran.ui.MovieDetailActivity
import com.example.yaran.webService.model.SearchModel
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter(val appCompatActivity: AppCompatActivity) :
    RecyclerView.Adapter<MovieAdapter.Holder>() {


    private val movies = mutableListOf<SearchModel.SearchResultModel>()

    fun submit(movieList: List<SearchModel.SearchResultModel>?) {
        movieList?.let {
            val oldSize = movies.size
            movies.addAll(movieList)
            notifyItemRangeInserted(oldSize, movieList.size)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return Holder(view, appCompatActivity)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val movie = movies[position]

        holder.movie = movie

        holder.tvTitle.text = movie.title

        holder.tvYear.text = StringUtils.getString(R.string.year, movie.year)

        holder.tvDescription.text = StringUtils.getString(R.string.lorem)

        GlideApp.with(holder.ivPoster).load(movie.poster).into(holder.ivPoster)

    }

    inner class Holder(
        itemView: View,
        private val appCompatActivity: AppCompatActivity
    ) :
        RecyclerView.ViewHolder(itemView) {

        lateinit var movie: SearchModel.SearchResultModel

        val tvTitle: AppCompatTextView = itemView.tv_title
        val tvYear: AppCompatTextView = itemView.tv_year
        val tvDescription: AppCompatTextView = itemView.tv_description
        val ivPoster: ImageView = itemView.iv_image

        init {
            itemView.setOnClickListener {

                val intent = Intent(appCompatActivity, MovieDetailActivity::class.java)

                intent.putExtra(MovieDetailActivity.KEY_IMDB_ID, movie.imdbID)
                intent.putExtra(MovieDetailActivity.KEY_TITLE, movie.title)
                intent.putExtra(MovieDetailActivity.KEY_POSTER_URL, movie.poster)

                val options = ActivityOptions
                    .makeSceneTransitionAnimation(
                        appCompatActivity,
                        Pair<View, String>(itemView.cv_cover, "poster"),
                        Pair<View, String>(itemView.tv_title, "title")
                    )

                appCompatActivity.startActivity(intent, options.toBundle())
            }
        }

    }
}