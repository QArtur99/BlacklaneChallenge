package com.qartf.popularmovies.data.network

import com.qartf.blacklanechallenge.data.model.Post
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface TheMovieDbApi {

    @GET("/posts")
    fun getPostsAsync(): Deferred<List<Post>>
}