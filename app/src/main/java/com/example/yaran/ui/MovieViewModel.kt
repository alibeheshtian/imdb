package com.example.yaran.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yaran.base.BaseViewModel
import com.example.yaran.webService.model.MovieModel
import com.example.yaran.webService.model.SearchModel

class MovieViewModel(context: Context) : BaseViewModel(context) {

    val moviesLiveData = MutableLiveData<List<SearchModel.SearchResultModel>>()
    val movieLiveData = MutableLiveData<MovieModel>()

    fun getMovies() {
        callService(apiService.movies(search = "batman"), onSuccess = {
            if (it.isSuccessful) {
                moviesLiveData.postValue(it.body()?.search)
            }
        })
    }

    fun getMovie(imdbId: String) {
        callService(apiService.movie(imdbID = imdbId), onSuccess = {
            if (it.isSuccessful) {
                movieLiveData.postValue(it.body())
            }
        })
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MovieViewModel(context) as T
        }
    }
}