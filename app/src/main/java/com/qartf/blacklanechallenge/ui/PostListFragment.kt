package com.qartf.blacklanechallenge.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.qartf.blacklanechallenge.R
import com.qartf.blacklanechallenge.data.model.Post
import com.qartf.blacklanechallenge.util.Constants.Companion.POST_LIST
import com.qartf.blacklanechallenge.util.Constants.Companion.RECYCLER_VIEW_STATE
import com.qartf.blacklanechallenge.util.MoshiConverter
import org.koin.android.ext.android.inject

class PostListFragment : Fragment(), PostContract.View {

    private val postPresenter: PostContract.Presenter by inject()

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private var postList: List<Post> = listOf()

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
        this.postList = postList
        val adapter = recyclerView.adapter as PostAdapter
        adapter.submitList(postList)
        adapter.notifyDataSetChanged()
        swipeRefresh.isRefreshing = false
    }

    override fun setException(exception: Exception) {
        swipeRefresh.isRefreshing = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val listState = recyclerView.layoutManager?.onSaveInstanceState()
        outState.putParcelable(RECYCLER_VIEW_STATE, listState)
        outState.putString(POST_LIST, MoshiConverter.convertListToStr(Post::class.java, postList))
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            restorePostList(savedInstanceState)
            restoreRecyclerViewState(savedInstanceState)
        } ?: postPresenter.getPostList()
    }

    private fun restoreRecyclerViewState(savedInstanceState: Bundle): Unit? {
        val listState: Parcelable? = savedInstanceState.getParcelable(RECYCLER_VIEW_STATE)
        return recyclerView.layoutManager?.onRestoreInstanceState(listState)
    }

    private fun restorePostList(savedInstanceState: Bundle) {
        val postListString = savedInstanceState.getString(POST_LIST)!!
        postList = MoshiConverter.convertListFromStr(Post::class.java, postListString)
        setPostList(postList)
    }
}