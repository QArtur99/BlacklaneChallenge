package com.qartf.popularmovies.data.network

import com.qartf.blacklanechallenge.data.model.Post
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface TheMovieDbApi {

    @GET("/posts")
    fun getPostsAsync(): Deferred<List<Post>>
}