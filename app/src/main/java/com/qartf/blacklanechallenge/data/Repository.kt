package com.qartf.blacklanechallenge.data

import com.qartf.blacklanechallenge.OpenForTesting
import com.qartf.blacklanechallenge.data.model.Post
import com.qartf.popularmovies.data.network.TheMovieDbApi
import java.util.concurrent.Executor

@OpenForTesting
class Repository(
    private val api: TheMovieDbApi,
    private val networkExecutor: Executor
) {

    suspend fun getPostsAsync(): List<Post> {
        return api.getPostsAsync().await()
    }
}