package com.qartf.blacklanechallenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.qartf.blacklanechallenge.data.Repository
import com.qartf.blacklanechallenge.data.model.Post
import com.qartf.blacklanechallenge.ui.PostContract
import com.qartf.blacklanechallenge.ui.PostPresenter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class PostPresenterTest {

    @Rule
    @JvmField
    @ExperimentalCoroutinesApi
    val mainCoroutineRule = MainCoroutineRule()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val postListRepository = Mockito.mock(Repository::class.java)
    private val postView = Mockito.mock(PostContract.View::class.java)
    private val postPresenter = PostPresenter(postListRepository)

    private val postList = mutableListOf<Post>().apply {
        add(Post(0, "title0", "body0", 0))
        add(Post(1, "title1", "body1", 1))
        add(Post(2, "title2", "body2", 2))
    }

    @Before
    fun init() {
        postPresenter.setView(postView)
    }

    @Test
    fun getPostsAsyncSuccess() = runBlocking {
        Mockito.`when`(postListRepository.getPostsAsync()).thenReturn(postList)
        postPresenter.getPostList()
        Mockito.verify(postListRepository).getPostsAsync()
        Mockito.verify(postView).setPostList(postList)
    }

    @Test
    fun getPostsAsyncFail() = runBlocking {
        val exception = Exception()
        Mockito.`when`(postListRepository.getPostsAsync()).thenReturn(postList)
        Mockito.doAnswer { throw exception }.`when`(postListRepository).getPostsAsync()
        postPresenter.getPostList()
        Mockito.verify(postListRepository).getPostsAsync()
        Mockito.verify(postView, Mockito.never()).setPostList(postList)
        Mockito.verify(postView).setException(exception)
    }

    @Test
    fun onClear() {
        postPresenter.onClear()
        runBlocking {
            val exception = Exception()
            Mockito.`when`(postListRepository.getPostsAsync()).thenReturn(postList)
            Mockito.doAnswer { throw exception }.`when`(postListRepository).getPostsAsync()
            postPresenter.getPostList()
            Mockito.verify(postListRepository, Mockito.never()).getPostsAsync()
            Mockito.verify(postView, Mockito.never()).setPostList(postList)
            Mockito.verify(postView, Mockito.never()).setException(exception)
        }
    }
}
