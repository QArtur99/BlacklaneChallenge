package com.qartf.blacklanechallenge.ui

import com.qartf.blacklanechallenge.data.Repository
import com.qartf.blacklanechallenge.data.model.Post
import kotlinx.coroutines.*

class PostPresenter(private val repository: Repository) : PostContract.Presenter {

    private var postPresenterJob = Job()
    private val uiScope = CoroutineScope(postPresenterJob + Dispatchers.Main)

    private var viewPost: PostContract.View? = null
    private var postList: List<Post>? = null

    override fun setView(viewPost: PostContract.View) {
        this.viewPost = viewPost
        postList?.let { viewPost.setPostList(it) }
    }

    override fun getPostList() {
        uiScope.launch {
            try {
                postList = repository.getPostsAsync()
                postList?.let { viewPost?.setPostList(it)}
            } catch (e: Exception) {
                viewPost?.setException(e)
            }
        }

    }

    override fun onDestroy() {
        uiScope.cancel()
        postPresenterJob.cancel()
    }

}