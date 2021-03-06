package com.qartf.blacklanechallenge.ui

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.qartf.blacklanechallenge.OpenForTesting
import com.qartf.blacklanechallenge.R
import com.qartf.blacklanechallenge.data.model.Post
import com.qartf.blacklanechallenge.util.ConnectionUtils
import com.qartf.blacklanechallenge.util.Constants
import com.qartf.blacklanechallenge.util.Constants.Companion.POST_LIST
import com.qartf.blacklanechallenge.util.Constants.Companion.RECYCLER_VIEW_STATE
import com.qartf.blacklanechallenge.util.MoshiConverter
import org.koin.android.ext.android.inject

@OpenForTesting
class PostListFragment : Fragment(), PostContract.View {

    private val postPresenter: PostContract.Presenter by inject()

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private var postList: List<Post> = listOf()

    internal lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_post_list, container, false)
        swipeRefresh = rootView.findViewById(R.id.swipe_refresh)
        recyclerView = rootView.findViewById(R.id.recyclerView)

        recyclerView.adapter = PostAdapter(getClickListener())
        postPresenter.setView(this)
        initSwipeToRefresh()
        setHasOptionsMenu(true)
        return rootView
    }

    private fun getClickListener() = object : PostAdapter.OnClickListener {
        override fun onClick(product: Post) {
            AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("Post id")
                .setMessage(product.id.toString())
                .setPositiveButton("Ok") { _, _ -> }
                .create().show()
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

    fun setSearchList(postList: List<Post>) {
        val adapter = recyclerView.adapter as PostAdapter
        adapter.submitList(postList)
        adapter.notifyDataSetChanged()
        swipeRefresh.isRefreshing = false
    }

    override fun setException(exception: Exception) {
        swipeRefresh.isRefreshing = false
        if (ConnectionUtils.isConnectedToInternet(activity!!).not()) {
            showSnackbar(Constants.NO_CONNECTION_ERROR_MESSAGE)
            return
        }
        when (exception) {
            else -> showSnackbar(exception::class.java.simpleName)
        }
    }

    private fun showSnackbar(text: String) {
        val view: View = activity!!.findViewById(android.R.id.content)
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        setSearchViewListener(searchView)
        setOnQueryTextFocusChangeListener(searchItem, searchView)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setOnQueryTextFocusChangeListener(searchItem: MenuItem, searchView: SearchView) {
        searchView.setOnQueryTextFocusChangeListener { view, b ->
            if (b.not()) searchItem.collapseActionView()
        }
    }

    private fun setSearchViewListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty().not()) {
                    setSearchList(postList.filter { it.title?.contains(newText) ?: false })
                }
                return true
            }
        })
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