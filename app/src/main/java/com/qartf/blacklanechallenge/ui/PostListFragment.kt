package com.qartf.blacklanechallenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.qartf.blacklanechallenge.R
import com.qartf.blacklanechallenge.data.model.Post
import org.koin.android.ext.android.inject
import java.lang.Exception

class PostListFragment : Fragment(), PostContract.View {

    private val postPresenter: PostContract.Presenter by inject()

    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_post_list, container, false)
        swipeRefresh = rootView.findViewById(R.id.swipe_refresh)
        recyclerView = rootView.findViewById(R.id.recyclerView)

        recyclerView.adapter = PostAdapter(getClickListener())
        postPresenter.setView(this)
        initSwipeToRefresh()
        return rootView
    }

    private fun getClickListener() = object : PostAdapter.OnClickListener {
        override fun onClick(product: Post) {

        }
    }

    private fun initSwipeToRefresh() {
        swipeRefresh.setOnRefreshListener {
            postPresenter.getPostList()
            swipeRefresh.isRefreshing = true
        }
    }

    override fun setPostList(postList: List<Post>) {
        swipeRefresh.isRefreshing = false
        val adapter = recyclerView.adapter as PostAdapter
        adapter.submitList(postList)
        adapter.notifyDataSetChanged()
    }

    override fun setException(exception: Exception) {
        swipeRefresh.isRefreshing = false
    }
}