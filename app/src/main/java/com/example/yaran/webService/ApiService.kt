package com.example.yaran.webService

import com.example.yaran.BuildConfig
import com.example.yaran.webService.model.MovieModel
import com.example.yaran.webService.model.SearchModel
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/")
    fun movies(
        @Query("apikey") apikey: String = BuildConfig.API_KEY,
        @Query("s") search: String
    ): Single<Response<SearchModel>>

    @GET("/")
    fun movie(
        @Query("apikey") apikey: String = BuildConfig.API_KEY,
        @Query("i") imdbID: String
    ): Single<Response<MovieModel>>


}