package com.qartf.blacklanechallenge.ui

import com.qartf.blacklanechallenge.data.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PostPresenter(private val repository: Repository) : PostContract.Presenter {

    private val postPresenterJob = Job()
    private val uiScope = CoroutineScope(postPresenterJob + Dispatchers.Main)

    private var viewPost: PostContract.View? = null

    override fun setView(viewPost: PostContract.View) {
        this.viewPost = viewPost
    }

    override fun getPostList() {
        uiScope.launch {
            try {
                val postList = repository.getPostsAsync()
                postList.let { viewPost?.setPostList(it) }
            } catch (e: Exception) {
                viewPost?.setException(e)
            }
        }
    }

    override fun onClear() {
        postPresenterJob.cancel()
    }
}