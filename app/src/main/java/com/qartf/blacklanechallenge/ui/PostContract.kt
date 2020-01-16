package com.qartf.blacklanechallenge.ui

import com.qartf.blacklanechallenge.data.model.Post
import java.lang.Exception

interface PostContract {

    interface Presenter {
        fun getPostList()
        fun setView(viewPost: View)
        fun onDestroy()
    }

    interface View {
        fun setPostList(postList: List<Post>)
        fun setException(exception: Exception)
    }
}