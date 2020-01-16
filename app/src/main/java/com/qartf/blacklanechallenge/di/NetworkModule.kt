package com.qartf.blacklanechallenge.di

import com.qartf.blacklanechallenge.data.Repository
import com.qartf.popularmovies.data.network.RetrofitModule
import com.qartf.popularmovies.data.network.TheMovieDbApi
import org.koin.dsl.module
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val NetworkModule = module {
    single { createTheMovieDbApi() }
    single { createNetworkExecutor() }
    single { createRepository(get(), get()) }
}

fun createTheMovieDbApi(): TheMovieDbApi {
    return RetrofitModule.devbytes
}

fun createNetworkExecutor(): Executor {
    return Executors.newFixedThreadPool(5)
}

fun createRepository(api: TheMovieDbApi, networkExecutor: Executor): Repository {
    return Repository(api, networkExecutor)
}
