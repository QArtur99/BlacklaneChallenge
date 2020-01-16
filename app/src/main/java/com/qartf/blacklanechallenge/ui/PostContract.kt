package com.qartf.blacklanechallenge.ui

import com.qartf.blacklanechallenge.data.model.Post

interface PostContract {

    interface Presenter {
        fun getPostList()
        fun setView(viewPost: View)
        fun onClear()
    }

    interface View {
        fun setPostList(postList: List<Post>)
        fun setException(exception: Exception)
    }
}