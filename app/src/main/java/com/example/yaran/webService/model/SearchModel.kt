package com.example.yaran.webService.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchModel(
    @SerializedName("Response")
    val response: String,
    @SerializedName("Search")
    val search: List<SearchResultModel>,
    @SerializedName("totalResults")
    val totalResults: String
) : Parcelable {
    @Parcelize
    data class SearchResultModel(
        @SerializedName("imdbID")
        val imdbID: String,
        @SerializedName("Poster")
        val poster: String,
        @SerializedName("Title")
        val title: String,
        @SerializedName("Type")
        val type: String,
        @SerializedName("Year")
        val year: String
    ) : Parcelable
}