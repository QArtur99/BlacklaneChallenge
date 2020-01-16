package com.qartf.blacklanechallenge.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qartf.blacklanechallenge.R
import com.qartf.blacklanechallenge.di.presenterModule
import com.qartf.blacklanechallenge.util.ext.setFirstFragment
import org.koin.android.ext.android.get
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class PostActivity : AppCompatActivity() {

    private val postPresenter by lazy { get<PostContract.Presenter>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadKoinModules(presenterModule)
        if (savedInstanceState == null) setFirstFragment(PostListFragment(), R.id.container)
    }

    override fun onDestroy() {
        postPresenter.onClear()
        unloadKoinModules(presenterModule)
        super.onDestroy()
    }
}
