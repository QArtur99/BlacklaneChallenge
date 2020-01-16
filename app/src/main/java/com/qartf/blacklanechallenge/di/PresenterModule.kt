package com.qartf.blacklanechallenge.di

import com.qartf.blacklanechallenge.data.Repository
import com.qartf.blacklanechallenge.ui.PostContract
import com.qartf.blacklanechallenge.ui.PostPresenter
import org.koin.dsl.module

val presenterModule = module {
    single { createPostPresenter(get()) }
}

fun createPostPresenter(repository: Repository): PostContract.Presenter {
    return PostPresenter(repository)
}