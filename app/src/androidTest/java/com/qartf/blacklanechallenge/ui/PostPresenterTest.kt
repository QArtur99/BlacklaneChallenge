package com.qartf.blacklanechallenge.ui

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.qartf.blacklanechallenge.R
import com.qartf.blacklanechallenge.SingleFragmentActivity
import com.qartf.blacklanechallenge.TestApp
import com.qartf.blacklanechallenge.data.Repository
import com.qartf.blacklanechallenge.data.model.Post
import com.qartf.blacklanechallenge.util.waitForAdapterChange
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doAnswer
import org.robolectric.annotation.LooperMode
import org.robolectric.annotation.TextLayoutMode

@RunWith(AndroidJUnit4::class)
@MediumTest
@LooperMode(LooperMode.Mode.PAUSED)
@TextLayoutMode(TextLayoutMode.Mode.REALISTIC)
@ExperimentalCoroutinesApi
class PostPresenterTest {

    @Rule
    @JvmField
    var countingRule = CountingTaskExecutorRule()

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)
    val application =
        InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApp
    private val postListRepository = Mockito.mock(Repository::class.java)
    private val postPresenter = PostPresenter(postListRepository) as PostContract.Presenter
    private lateinit var postListFragment: TestPostListFragment

    private val postList = mutableListOf<Post>().apply {
        add(Post(0, "title0", "body0", 0))
        add(Post(1, "title1", "body1", 1))
        add(Post(2, "title2", "body2", 2))
    }

    @Before
    fun init() {
        application.injectModule(
            module { single(override = true) { postPresenter } }
        )
        postListFragment = TestPostListFragment()
        activityRule.activity.setFragment(postListFragment)
    }

    @Test
    fun setPostListSuccess() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        runBlocking { `when`(postListRepository.getPostsAsync()).thenReturn(postList) }
        postPresenter.getPostList()
        val recyclerView: RecyclerView = postListFragment.rootView.findViewById(R.id.recyclerView)
        waitForAdapterChange(recyclerView, countingRule)
        onView(withId(R.id.recyclerView)).check(matches(hasChildCount(3)))
    }

    @Test
    fun setPostListFail() {
        val exception = Exception()
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        runBlocking { doAnswer { throw exception }.`when`(postListRepository).getPostsAsync() }
        postPresenter.getPostList()
        Thread.sleep(600)
        onView(withText(exception::class.java.simpleName)).check(matches(isDisplayed()))
    }

    @Test
    fun itemClick() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        runBlocking { `when`(postListRepository.getPostsAsync()).thenReturn(postList) }
        postPresenter.getPostList()
        val recyclerView: RecyclerView = postListFragment.rootView.findViewById(R.id.recyclerView)
        waitForAdapterChange(recyclerView, countingRule)
        onView(withId(R.id.recyclerView)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
        onView(withId(android.R.id.button1)).check(matches(isDisplayed()))
        onView(withText(postList[0].id.toString())).check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())
        onView(withId(android.R.id.button1)).check(doesNotExist())
    }

    class TestPostListFragment : PostListFragment()
}
